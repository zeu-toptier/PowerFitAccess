package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Pagamento;
import com.powerfit.access.model.Plano;
import com.powerfit.access.model.StatusPagamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagamentoDAO {

    private static final String SELECT_COMPLETO = """
            SELECT
                pg.id AS pagamento_id,
                pg.valor AS pagamento_valor,
                pg.data_pagamento,
                pg.data_vencimento,
                pg.status AS pagamento_status,

                m.id AS matricula_id,
                m.numero_matricula,
                m.data_inicio,
                m.data_fim,
                m.ativa AS matricula_ativa,

                a.id AS aluno_id,
                a.nome AS aluno_nome,
                a.cpf AS aluno_cpf,
                a.telefone AS aluno_telefone,
                a.email AS aluno_email,
                a.ativo AS aluno_ativo,
                a.data_cadastro AS aluno_data_cadastro,

                p.id AS plano_id,
                p.nome AS plano_nome,
                p.descricao AS plano_descricao,
                p.valor_mensal AS plano_valor_mensal,
                p.duracao_dias AS plano_duracao_dias,
                p.ativo AS plano_ativo

            FROM pagamentos pg
            INNER JOIN matriculas m ON pg.matricula_id = m.id
            INNER JOIN alunos a ON m.aluno_id = a.id
            INNER JOIN planos p ON m.plano_id = p.id
            """;

    public void cadastrar(Pagamento pagamento) throws SQLException {

        String sql = """
                INSERT INTO pagamentos
                (matricula_id, valor, data_pagamento,
                 data_vencimento, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setLong(1, pagamento.getMatricula().getId());
            comando.setBigDecimal(2, pagamento.getValor());

            if (pagamento.getDataPagamento() == null) {
                comando.setDate(3, null);
            } else {
                comando.setDate(
                        3, Date.valueOf(pagamento.getDataPagamento())
                );
            }

            comando.setDate(
                    4, Date.valueOf(pagamento.getDataVencimento())
            );
            comando.setString(5, pagamento.getStatus().name());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    pagamento.setId(resultado.getLong(1));
                }
            }
        }
    }

    public Optional<Pagamento> buscarPorId(Long id) throws SQLException {

        String sql = SELECT_COMPLETO + " WHERE pg.id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearPagamento(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public List<Pagamento> listarTodos() throws SQLException {

        String sql = SELECT_COMPLETO
                + " ORDER BY pg.data_vencimento DESC";

        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                pagamentos.add(mapearPagamento(resultado));
            }
        }

        return pagamentos;
    }

    public List<Pagamento> listarPorMatricula(
            Long matriculaId) throws SQLException {

        String sql = SELECT_COMPLETO
                + " WHERE pg.matricula_id = ?"
                + " ORDER BY pg.data_vencimento DESC";

        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, matriculaId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    pagamentos.add(mapearPagamento(resultado));
                }
            }
        }

        return pagamentos;
    }

    public List<Pagamento> listarPorStatus(
            StatusPagamento status) throws SQLException {

        String sql = SELECT_COMPLETO
                + " WHERE pg.status = ?"
                + " ORDER BY pg.data_vencimento";

        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, status.name());

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    pagamentos.add(mapearPagamento(resultado));
                }
            }
        }

        return pagamentos;
    }

    public List<Pagamento> listarPendentes() throws SQLException {
        return listarPorStatus(StatusPagamento.PENDENTE);
    }

    public List<Pagamento> listarAtrasados() throws SQLException {
        return listarPorStatus(StatusPagamento.ATRASADO);
    }

    public boolean atualizar(Pagamento pagamento) throws SQLException {

        if (pagamento.getId() == null) {
            throw new IllegalArgumentException(
                    "O ID do pagamento é obrigatório para atualização."
            );
        }

        String sql = """
                UPDATE pagamentos
                SET matricula_id = ?, valor = ?, data_pagamento = ?,
                    data_vencimento = ?, status = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, pagamento.getMatricula().getId());
            comando.setBigDecimal(2, pagamento.getValor());

            if (pagamento.getDataPagamento() == null) {
                comando.setDate(3, null);
            } else {
                comando.setDate(
                        3, Date.valueOf(pagamento.getDataPagamento())
                );
            }

            comando.setDate(
                    4, Date.valueOf(pagamento.getDataVencimento())
            );
            comando.setString(5, pagamento.getStatus().name());
            comando.setLong(6, pagamento.getId());

            return comando.executeUpdate() > 0;
        }
    }

    private Pagamento mapearPagamento(ResultSet resultado)
            throws SQLException {

        Aluno aluno = new Aluno(
                resultado.getLong("aluno_id"),
                resultado.getString("aluno_nome"),
                resultado.getString("aluno_cpf"),
                resultado.getString("aluno_telefone"),
                resultado.getString("aluno_email"),
                resultado.getBoolean("aluno_ativo"),
                resultado.getDate("aluno_data_cadastro").toLocalDate()
        );

        Plano plano = new Plano(
                resultado.getLong("plano_id"),
                resultado.getString("plano_nome"),
                resultado.getString("plano_descricao"),
                resultado.getBigDecimal("plano_valor_mensal"),
                resultado.getInt("plano_duracao_dias"),
                resultado.getBoolean("plano_ativo")
        );

        Matricula matricula = new Matricula(
                resultado.getLong("matricula_id"),
                resultado.getString("numero_matricula"),
                aluno,
                plano,
                resultado.getDate("data_inicio").toLocalDate(),
                resultado.getDate("data_fim").toLocalDate(),
                resultado.getBoolean("matricula_ativa")
        );

        Date dataPagamentoSql = resultado.getDate("data_pagamento");

        return new Pagamento(
                resultado.getLong("pagamento_id"),
                matricula,
                resultado.getBigDecimal("pagamento_valor"),
                dataPagamentoSql == null
                        ? null : dataPagamentoSql.toLocalDate(),
                resultado.getDate("data_vencimento").toLocalDate(),
                StatusPagamento.valueOf(
                        resultado.getString("pagamento_status")
                )
        );
    }
}