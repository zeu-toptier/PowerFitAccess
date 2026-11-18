package com.powerfit.access.dao;

import com.powerfit.access.model.Aluno;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.powerfit.access.database.ConexaoBanco;

class AlunoDAOIntegracaoTest {

    private final AlunoDAO alunoDAO = new AlunoDAO();

    private Aluno aluno;

    @BeforeEach
    void prepararTeste() throws SQLException {
        aluno = new Aluno(
                "Aluno Teste",
                "99999999999",
                "11999999999",
                "teste@powerfit.com"
        );

        alunoDAO.cadastrar(aluno);
    }

    @AfterEach
    void limparTeste() throws SQLException {
        if (aluno.getId() != null) {
            String sql = "DELETE FROM alunos WHERE id = ?";

            try (Connection conexao = ConexaoBanco.conectar();
                 PreparedStatement comando = conexao.prepareStatement(sql)) {

                comando.setLong(1, aluno.getId());
                comando.executeUpdate();
            }
        }
    }

    @Test
    void deveCadastrarAluno() {
        assertNotNull(aluno.getId());
        assertTrue(aluno.getId() > 0);
    }

    @Test
    void deveBuscarAlunoPorId() throws SQLException {
        Optional<Aluno> resultado =
                alunoDAO.buscarPorId(aluno.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Aluno Teste", resultado.get().getNome());
    }

    @Test
    void deveBuscarAlunoPorCpf() throws SQLException {
        Optional<Aluno> resultado =
                alunoDAO.buscarPorCpf("99999999999");

        assertTrue(resultado.isPresent());
        assertEquals(aluno.getId(), resultado.get().getId());
    }

    @Test
    void deveListarAlunos() throws SQLException {
        List<Aluno> alunos = alunoDAO.listarTodos();

        assertTrue(alunos.stream().anyMatch(
                item -> item.getId().equals(aluno.getId())
        ));
    }

    @Test
    void deveAtualizarAluno() throws SQLException {
        aluno.setNome("Aluno Atualizado");

        boolean atualizado = alunoDAO.atualizar(aluno);

        assertTrue(atualizado);

        Optional<Aluno> resultado =
                alunoDAO.buscarPorId(aluno.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Aluno Atualizado", resultado.get().getNome());
    }

    @Test
    void deveDesativarAluno() throws SQLException {
        boolean desativado = alunoDAO.desativar(aluno.getId());

        assertTrue(desativado);

        Optional<Aluno> resultado =
                alunoDAO.buscarPorId(aluno.getId());

        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().isAtivo());
    }
}