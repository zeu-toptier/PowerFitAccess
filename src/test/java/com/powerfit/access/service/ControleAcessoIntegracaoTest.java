package com.powerfit.access.service;

import com.powerfit.access.dao.AlunoDAO;
import com.powerfit.access.dao.MatriculaDAO;
import com.powerfit.access.dao.PagamentoDAO;
import com.powerfit.access.dao.PlanoDAO;
import com.powerfit.access.dao.RegistroAcessoDAO;
import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Pagamento;
import com.powerfit.access.model.Plano;
import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.ResultadoAcesso;
import com.powerfit.access.model.StatusPagamento;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ControleAcessoIntegracaoTest {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PlanoDAO planoDAO = new PlanoDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private final RegistroAcessoDAO registroDAO = new RegistroAcessoDAO();

    private final ControleAcesso controle = new ControleAcesso();

    private Aluno aluno;
    private Plano plano;
    private Matricula matricula;

    private final List<Pagamento> pagamentosCriados = new ArrayList<>();
    private final List<RegistroAcesso> registrosCriados = new ArrayList<>();

    @BeforeEach
    void prepararTeste() throws SQLException {

        aluno = new Aluno(
                "Aluno Teste Controle",
                "99999999995",
                "11999999995",
                "controle.teste@email.com"
        );
        alunoDAO.cadastrar(aluno);

        plano = new Plano(
                "Plano Teste Controle",
                "Plano utilizado nos testes de controle de acesso",
                new BigDecimal("99.90"),
                30
        );
        planoDAO.cadastrar(plano);

        matricula = new Matricula(
                "MAT-CONTROLE-TESTE-001",
                aluno,
                plano,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(30)
        );
        matriculaDAO.cadastrar(matricula);
    }

    private Pagamento criarPagamento(LocalDate vencimento)
            throws SQLException {

        Pagamento pagamento = new Pagamento(
                matricula,
                new BigDecimal("99.90"),
                vencimento
        );

        pagamentoDAO.cadastrar(pagamento);
        pagamentosCriados.add(pagamento);

        return pagamento;
    }

    private RegistroAcesso verificar() throws SQLException {

        RegistroAcesso registro =
                controle.verificarAcesso(matricula.getNumeroMatricula());

        registrosCriados.add(registro);
        return registro;
    }

    @AfterEach
    void limparTeste() throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar()) {

            for (RegistroAcesso registro : registrosCriados) {
                if (registro.getId() != null) {
                    try (PreparedStatement comando = conexao.prepareStatement(
                            "DELETE FROM registros_acesso WHERE id = ?")) {
                        comando.setLong(1, registro.getId());
                        comando.executeUpdate();
                    }
                }
            }

            for (Pagamento pagamento : pagamentosCriados) {
                if (pagamento.getId() != null) {
                    try (PreparedStatement comando = conexao.prepareStatement(
                            "DELETE FROM pagamentos WHERE id = ?")) {
                        comando.setLong(1, pagamento.getId());
                        comando.executeUpdate();
                    }
                }
            }

            if (matricula != null && matricula.getId() != null) {
                try (PreparedStatement comando = conexao.prepareStatement(
                        "DELETE FROM matriculas WHERE id = ?")) {
                    comando.setLong(1, matricula.getId());
                    comando.executeUpdate();
                }
            }

            if (aluno != null && aluno.getId() != null) {
                try (PreparedStatement comando = conexao.prepareStatement(
                        "DELETE FROM alunos WHERE id = ?")) {
                    comando.setLong(1, aluno.getId());
                    comando.executeUpdate();
                }
            }

            if (plano != null && plano.getId() != null) {
                try (PreparedStatement comando = conexao.prepareStatement(
                        "DELETE FROM planos WHERE id = ?")) {
                    comando.setLong(1, plano.getId());
                    comando.executeUpdate();
                }
            }
        }
    }

    @Test
    void deveAutorizarAcessoSemPagamentosPendentes()
            throws SQLException {

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.AUTORIZADO, registro.getResultado());
        assertEquals("Acesso autorizado.", registro.getMotivo());
        assertNotNull(registro.getId());
    }

    @Test
    void deveNegarMatriculaInexistente() throws SQLException {

        RegistroAcesso registro =
                controle.verificarAcesso("MAT-INEXISTENTE-999");

        registrosCriados.add(registro);

        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
        assertNull(registro.getAluno());
        assertEquals("Matrícula não encontrada.", registro.getMotivo());
        assertNotNull(registro.getId());
    }

    @Test
    void deveNegarAlunoInativo() throws SQLException {

        aluno.desativar();
        assertTrue(alunoDAO.atualizar(aluno));

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
        assertEquals("Aluno inativo.", registro.getMotivo());
    }

    @Test
    void deveNegarMatriculaInativa() throws SQLException {

        assertTrue(matriculaDAO.desativar(matricula.getId()));

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
        assertEquals("Matrícula inativa.", registro.getMotivo());
    }

    @Test
    void deveNegarMatriculaForaDaValidade() throws SQLException {

        matricula.setDataFim(LocalDate.now().minusDays(1));
        assertTrue(matriculaDAO.atualizar(matricula));

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
        assertEquals(
                "Matrícula fora do período de validade.",
                registro.getMotivo()
        );
    }

    @Test
    void deveNegarPagamentoPendente() throws SQLException {

        criarPagamento(LocalDate.now().plusDays(5));

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
        assertEquals(
                "Existe pagamento pendente ou atrasado.",
                registro.getMotivo()
        );
    }

    @Test
    void deveAtualizarPagamentoVencidoEnegarAcesso()
            throws SQLException {

        Pagamento pagamento =
                criarPagamento(LocalDate.now().minusDays(1));

        RegistroAcesso registro = verificar();

        Optional<Pagamento> atualizado =
                pagamentoDAO.buscarPorId(pagamento.getId());

        assertTrue(atualizado.isPresent());
        assertEquals(
                StatusPagamento.ATRASADO,
                atualizado.get().getStatus()
        );
        assertEquals(ResultadoAcesso.NEGADO, registro.getResultado());
    }

    @Test
    void deveAutorizarAcessoQuandoPagamentoEstiverPago()
            throws SQLException {

        Pagamento pagamento =
                criarPagamento(LocalDate.now().plusDays(5));

        pagamento.registrarPagamento();
        assertTrue(pagamentoDAO.atualizar(pagamento));

        RegistroAcesso registro = verificar();

        assertEquals(ResultadoAcesso.AUTORIZADO, registro.getResultado());
    }
}
