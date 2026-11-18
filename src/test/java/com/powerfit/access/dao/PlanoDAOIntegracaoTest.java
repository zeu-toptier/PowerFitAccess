package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Plano;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlanoDAOIntegracaoTest {

    private final PlanoDAO planoDAO = new PlanoDAO();

    private Plano plano;

    @BeforeEach
    void prepararTeste() throws SQLException {

        plano = new Plano(
                "Plano Teste",
                "Plano utilizado nos testes de integração",
                new BigDecimal("99.90"),
                30
        );

        planoDAO.cadastrar(plano);
    }

    @AfterEach
    void limparTeste() throws SQLException {

        if (plano.getId() != null) {

            String sql = "DELETE FROM planos WHERE id = ?";

            try (Connection conexao = ConexaoBanco.conectar();
                 PreparedStatement comando = conexao.prepareStatement(sql)) {

                comando.setLong(1, plano.getId());
                comando.executeUpdate();
            }
        }
    }

    @Test
    void deveCadastrarPlano() {

        assertNotNull(plano.getId());
        assertTrue(plano.getId() > 0);
    }

    @Test
    void deveBuscarPlanoPorId() throws SQLException {

        Optional<Plano> resultado =
                planoDAO.buscarPorId(plano.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Plano Teste", resultado.get().getNome());
    }

    @Test
    void deveListarPlanos() throws SQLException {

        List<Plano> planos = planoDAO.listarTodos();

        assertTrue(planos.stream().anyMatch(
                item -> item.getId().equals(plano.getId())
        ));
    }

    @Test
    void deveListarPlanosAtivos() throws SQLException {

        List<Plano> planos = planoDAO.listarAtivos();

        assertTrue(planos.stream().anyMatch(
                item -> item.getId().equals(plano.getId())
        ));
    }

    @Test
    void deveAtualizarPlano() throws SQLException {

        plano.setNome("Plano Atualizado");

        boolean atualizado = planoDAO.atualizar(plano);

        assertTrue(atualizado);

        Optional<Plano> resultado =
                planoDAO.buscarPorId(plano.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Plano Atualizado", resultado.get().getNome());
    }

    @Test
    void deveDesativarPlano() throws SQLException {

        boolean desativado = planoDAO.desativar(plano.getId());

        assertTrue(desativado);

        Optional<Plano> resultado =
                planoDAO.buscarPorId(plano.getId());

        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().isAtivo());
    }
}