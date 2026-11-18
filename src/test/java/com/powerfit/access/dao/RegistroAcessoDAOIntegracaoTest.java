package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.ResultadoAcesso;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegistroAcessoDAOIntegracaoTest {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final RegistroAcessoDAO registroDAO = new RegistroAcessoDAO();

    private Aluno aluno;
    private RegistroAcesso registro;

    private final List<RegistroAcesso> registrosCriados = new ArrayList<>();

    @BeforeEach
    void prepararTeste() throws SQLException {

        aluno = new Aluno(
                "Aluno Teste Registro",
                "99999999996",
                "11999999996",
                "registro.teste@email.com"
        );

        alunoDAO.cadastrar(aluno);

        registro = registrar(
                aluno,
                "MAT-REG-TESTE-001",
                ResultadoAcesso.AUTORIZADO,
                "Acesso autorizado para teste"
        );
    }

    private RegistroAcesso registrar(
            Aluno aluno,
            String numeroMatricula,
            ResultadoAcesso resultado,
            String motivo
    ) throws SQLException {

        RegistroAcesso novoRegistro = new RegistroAcesso(
                aluno,
                numeroMatricula,
                resultado,
                motivo
        );

        registroDAO.registrar(novoRegistro);
        registrosCriados.add(novoRegistro);

        return novoRegistro;
    }

    @AfterEach
    void limparTeste() throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar()) {

            for (RegistroAcesso item : registrosCriados) {

                if (item.getId() != null) {

                    try (PreparedStatement comando = conexao.prepareStatement(
                            "DELETE FROM registros_acesso WHERE id = ?")) {

                        comando.setLong(1, item.getId());
                        comando.executeUpdate();
                    }
                }
            }

            if (aluno != null && aluno.getId() != null) {

                try (PreparedStatement comando = conexao.prepareStatement(
                        "DELETE FROM alunos WHERE id = ?")) {

                    comando.setLong(1, aluno.getId());
                    comando.executeUpdate();
                }
            }
        }
    }

    @Test
    void deveRegistrarAcesso() {

        assertNotNull(registro.getId());
        assertTrue(registro.getId() > 0);
        assertEquals(
                ResultadoAcesso.AUTORIZADO,
                registro.getResultado()
        );
    }

    @Test
    void deveBuscarRegistroPorId() throws SQLException {

        Optional<RegistroAcesso> resultado =
                registroDAO.buscarPorId(registro.getId());

        assertTrue(resultado.isPresent());
        assertEquals(registro.getId(), resultado.get().getId());
        assertEquals(
                "MAT-REG-TESTE-001",
                resultado.get().getNumeroMatricula()
        );
        assertEquals(
                ResultadoAcesso.AUTORIZADO,
                resultado.get().getResultado()
        );
    }

    @Test
    void deveListarTodosOsRegistros() throws SQLException {

        List<RegistroAcesso> registros = registroDAO.listarTodos();

        assertTrue(registros.stream().anyMatch(
                item -> item.getId().equals(registro.getId())
        ));
    }

    @Test
    void deveListarRegistrosPorAluno() throws SQLException {

        List<RegistroAcesso> registros =
                registroDAO.listarPorAluno(aluno.getId());

        assertTrue(registros.stream().anyMatch(
                item -> item.getId().equals(registro.getId())
        ));
    }

    @Test
    void deveListarAcessosAutorizados() throws SQLException {

        List<RegistroAcesso> registros =
                registroDAO.listarAutorizados();

        assertTrue(registros.stream().anyMatch(
                item -> item.getId().equals(registro.getId())
        ));
    }

    @Test
    void deveListarAcessosNegados() throws SQLException {

        RegistroAcesso negado = registrar(
                aluno,
                "MAT-REG-TESTE-002",
                ResultadoAcesso.NEGADO,
                "Pagamento pendente"
        );

        List<RegistroAcesso> registros = registroDAO.listarNegados();

        assertTrue(registros.stream().anyMatch(
                item -> item.getId().equals(negado.getId())
        ));
    }

    @Test
    void deveRegistrarAcessoDeMatriculaDesconhecida()
            throws SQLException {

        RegistroAcesso desconhecido = registrar(
                null,
                "MAT-INEXISTENTE-001",
                ResultadoAcesso.NEGADO,
                "Matrícula não encontrada"
        );

        Optional<RegistroAcesso> resultado =
                registroDAO.buscarPorId(desconhecido.getId());

        assertTrue(resultado.isPresent());
        assertNull(resultado.get().getAluno());
        assertEquals(
                "MAT-INEXISTENTE-001",
                resultado.get().getNumeroMatricula()
        );
        assertEquals(
                ResultadoAcesso.NEGADO,
                resultado.get().getResultado()
        );
    }
}