package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Pagamento;
import com.powerfit.access.model.Plano;
import com.powerfit.access.model.StatusPagamento;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoDAOIntegracaoTest {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PlanoDAO planoDAO = new PlanoDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    private Aluno aluno;
    private Plano plano;
    private Matricula matricula;
    private Pagamento pagamento;

    @BeforeEach
    void prepararTeste() throws SQLException {

        aluno = new Aluno(
                "Aluno Teste Pagamento",
                "99999999997",
                "11999999997",
                "pagamento.teste@email.com"
        );

        alunoDAO.cadastrar(aluno);

        plano = new Plano(
                "Plano Teste Pagamento",
                "Plano utilizado nos testes de pagamento",
                new BigDecimal("99.90"),
                30
        );

        planoDAO.cadastrar(plano);

        matricula = new Matricula(
                "MAT-PAG-TESTE-001",
                aluno,
                plano,
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );

        matriculaDAO.cadastrar(matricula);

        pagamento = new Pagamento(
                matricula,
                new BigDecimal("99.90"),
                LocalDate.now().plusDays(30)
        );

        pagamentoDAO.cadastrar(pagamento);
    }

    @AfterEach
    void limparTeste() throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar()) {

            if (pagamento != null && pagamento.getId() != null) {

                try (PreparedStatement comando = conexao.prepareStatement(
                        "DELETE FROM pagamentos WHERE id = ?")) {

                    comando.setLong(1, pagamento.getId());
                    comando.executeUpdate();
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
    void deveCadastrarPagamento() {

        assertNotNull(pagamento.getId());
        assertTrue(pagamento.getId() > 0);
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
    }

    @Test
    void deveBuscarPagamentoPorId() throws SQLException {

        Optional<Pagamento> resultado =
                pagamentoDAO.buscarPorId(pagamento.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                pagamento.getId(),
                resultado.get().getId()
        );
        assertEquals(
                new BigDecimal("99.90"),
                resultado.get().getValor()
        );
        assertEquals(
                "MAT-PAG-TESTE-001",
                resultado.get().getMatricula().getNumeroMatricula()
        );
    }

    @Test
    void deveListarPagamentos() throws SQLException {

        List<Pagamento> pagamentos = pagamentoDAO.listarTodos();

        assertTrue(pagamentos.stream().anyMatch(
                item -> item.getId().equals(pagamento.getId())
        ));
    }

    @Test
    void deveListarPagamentosPorMatricula() throws SQLException {

        List<Pagamento> pagamentos =
                pagamentoDAO.listarPorMatricula(matricula.getId());

        assertTrue(pagamentos.stream().anyMatch(
                item -> item.getId().equals(pagamento.getId())
        ));
    }

    @Test
    void deveListarPagamentosPendentes() throws SQLException {

        List<Pagamento> pagamentos = pagamentoDAO.listarPendentes();

        assertTrue(pagamentos.stream().anyMatch(
                item -> item.getId().equals(pagamento.getId())
        ));
    }

    @Test
    void deveListarPagamentosAtrasados() throws SQLException {

        pagamento.marcarComoAtrasado();

        boolean atualizado = pagamentoDAO.atualizar(pagamento);

        assertTrue(atualizado);

        List<Pagamento> pagamentos = pagamentoDAO.listarAtrasados();

        assertTrue(pagamentos.stream().anyMatch(
                item -> item.getId().equals(pagamento.getId())
        ));
    }

    @Test
    void deveAtualizarPagamento() throws SQLException {

        pagamento.registrarPagamento();

        boolean atualizado = pagamentoDAO.atualizar(pagamento);

        assertTrue(atualizado);

        Optional<Pagamento> resultado =
                pagamentoDAO.buscarPorId(pagamento.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                StatusPagamento.PAGO,
                resultado.get().getStatus()
        );
        assertNotNull(resultado.get().getDataPagamento());
    }
}