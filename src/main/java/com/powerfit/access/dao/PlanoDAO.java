package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Plano;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlanoDAO {

    public void cadastrar(Plano plano) throws SQLException {

        String sql = """
                INSERT INTO planos
                (nome, descricao, valor_mensal, duracao_dias, ativo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, plano.getNome());
            comando.setString(2, plano.getDescricao());
            comando.setBigDecimal(3, plano.getValorMensal());
            comando.setInt(4, plano.getDuracaoDias());
            comando.setBoolean(5, plano.isAtivo());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    plano.setId(resultado.getLong(1));
                }
            }
        }
    }

    public Optional<Plano> buscarPorId(Long id) throws SQLException {

        String sql = "SELECT * FROM planos WHERE id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearPlano(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public List<Plano> listarTodos() throws SQLException {

        String sql = "SELECT * FROM planos ORDER BY nome";

        List<Plano> planos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                planos.add(mapearPlano(resultado));
            }
        }

        return planos;
    }

    public List<Plano> listarAtivos() throws SQLException {

        String sql = "SELECT * FROM planos WHERE ativo = TRUE ORDER BY nome";

        List<Plano> planos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                planos.add(mapearPlano(resultado));
            }
        }

        return planos;
    }

    public boolean atualizar(Plano plano) throws SQLException {

        if (plano.getId() == null) {
            throw new IllegalArgumentException(
                    "O ID do plano é obrigatório para atualização."
            );
        }

        String sql = """
                UPDATE planos
                SET nome = ?, descricao = ?, valor_mensal = ?,
                    duracao_dias = ?, ativo = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, plano.getNome());
            comando.setString(2, plano.getDescricao());
            comando.setBigDecimal(3, plano.getValorMensal());
            comando.setInt(4, plano.getDuracaoDias());
            comando.setBoolean(5, plano.isAtivo());
            comando.setLong(6, plano.getId());

            return comando.executeUpdate() > 0;
        }
    }

    public boolean desativar(Long id) throws SQLException {

        String sql = "UPDATE planos SET ativo = FALSE WHERE id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            return comando.executeUpdate() > 0;
        }
    }

    private Plano mapearPlano(ResultSet resultado) throws SQLException {

        return new Plano(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getString("descricao"),
                resultado.getBigDecimal("valor_mensal"),
                resultado.getInt("duracao_dias"),
                resultado.getBoolean("ativo")
        );
    }
}