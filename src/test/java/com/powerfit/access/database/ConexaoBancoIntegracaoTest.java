package com.powerfit.access.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class ConexaoBancoIntegracaoTest {

    @Test
    void deveConectarAoBancoDeDados() throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar()) {

            assertTrue(
                    conexao.isValid(5),
                    "A conexão com o banco de dados deveria estar válida."
            );

            String sql = "SELECT DATABASE()";

            try (PreparedStatement comando = conexao.prepareStatement(sql);
                 ResultSet resultado = comando.executeQuery()) {

                assertTrue(resultado.next());

                assertEquals(
                        "powerfit_access",
                        resultado.getString(1)
                );
            }
        }
    }
}