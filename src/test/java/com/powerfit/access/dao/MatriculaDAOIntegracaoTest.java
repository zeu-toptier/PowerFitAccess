package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Plano;

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

class MatriculaDAOIntegracaoTest {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PlanoDAO planoDAO = new PlanoDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    private Aluno aluno;
    private Plano plano;
    private Matricula matricula;

    @BeforeEach
    void prepararTeste() throws SQLException {

        aluno = new Aluno(
                "Aluno Teste Matricula",
                "99999999998",
                "11999999998",
                "matricula.teste@email.com"
        );

        alunoDAO.cadastrar(aluno);

        plano = new Plano(
                "Plano Teste Matricula",
                "Plano utilizado nos testes de matrícula",
                new BigDecimal("99.90"),
                30
        );

        planoDAO.cadastrar(plano);

        matricula = new Matricula(
                "MAT-TESTE-001",
                aluno,
                plano,
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );

        matriculaDAO.cadastrar(matricula);
    }

    @AfterEach
    void limparTeste() throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar()) {

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
    void deveCadastrarMatricula() {

        assertNotNull(matricula.getId());
        assertTrue(matricula.getId() > 0);
    }

    @Test
    void deveBuscarMatriculaPorId() throws SQLException {

        Optional<Matricula> resultado =
                matriculaDAO.buscarPorId(matricula.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                "MAT-TESTE-001",
                resultado.get().getNumeroMatricula()
        );
        assertEquals(
                "Aluno Teste Matricula",
                resultado.get().getAluno().getNome()
        );
        assertEquals(
                "Plano Teste Matricula",
                resultado.get().getPlano().getNome()
        );
    }

    @Test
    void deveBuscarMatriculaPorNumero() throws SQLException {

        Optional<Matricula> resultado =
                matriculaDAO.buscarPorNumeroMatricula(
                        "MAT-TESTE-001"
                );

        assertTrue(resultado.isPresent());
        assertEquals(
                matricula.getId(),
                resultado.get().getId()
        );
    }

    @Test
    void deveListarMatriculas() throws SQLException {

        List<Matricula> matriculas = matriculaDAO.listarTodos();

        assertTrue(matriculas.stream().anyMatch(
                item -> item.getId().equals(matricula.getId())
        ));
    }

    @Test
    void deveListarMatriculasPorAluno() throws SQLException {

        List<Matricula> matriculas =
                matriculaDAO.listarPorAluno(aluno.getId());

        assertTrue(matriculas.stream().anyMatch(
                item -> item.getId().equals(matricula.getId())
        ));
    }

    @Test
    void deveAtualizarMatricula() throws SQLException {

        matricula.setNumeroMatricula("MAT-TESTE-002");

        boolean atualizado = matriculaDAO.atualizar(matricula);

        assertTrue(atualizado);

        Optional<Matricula> resultado =
                matriculaDAO.buscarPorId(matricula.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                "MAT-TESTE-002",
                resultado.get().getNumeroMatricula()
        );
    }

    @Test
    void deveDesativarMatricula() throws SQLException {

        boolean desativado =
                matriculaDAO.desativar(matricula.getId());

        assertTrue(desativado);

        Optional<Matricula> resultado =
                matriculaDAO.buscarPorId(matricula.getId());

        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().isAtiva());
    }
}