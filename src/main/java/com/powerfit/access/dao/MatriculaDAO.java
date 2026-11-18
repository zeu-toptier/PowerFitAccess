package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Plano;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatriculaDAO {

    private static final String SELECT_COMPLETO = """
            SELECT
                m.id AS matricula_id,
                m.numero_matricula,
                m.data_inicio,
                m.data_fim,
                m.ativa,

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

            FROM matriculas m
            INNER JOIN alunos a ON m.aluno_id = a.id
            INNER JOIN planos p ON m.plano_id = p.id
            """;

    public void cadastrar(Matricula matricula) throws SQLException {

        String sql = """
                INSERT INTO matriculas
                (numero_matricula, aluno_id, plano_id,
                 data_inicio, data_fim, ativa)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, matricula.getNumeroMatricula());
            comando.setLong(2, matricula.getAluno().getId());
            comando.setLong(3, matricula.getPlano().getId());
            comando.setDate(4, Date.valueOf(matricula.getDataInicio()));
            comando.setDate(5, Date.valueOf(matricula.getDataFim()));
            comando.setBoolean(6, matricula.isAtiva());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    matricula.setId(resultado.getLong(1));
                }
            }
        }
    }

    public Optional<Matricula> buscarPorId(Long id) throws SQLException {

        String sql = SELECT_COMPLETO + " WHERE m.id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMatricula(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Matricula> buscarPorNumeroMatricula(
            String numeroMatricula) throws SQLException {

        String sql = SELECT_COMPLETO + " WHERE m.numero_matricula = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, numeroMatricula);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMatricula(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public List<Matricula> listarTodos() throws SQLException {

        String sql = SELECT_COMPLETO + " ORDER BY m.data_inicio DESC";

        List<Matricula> matriculas = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                matriculas.add(mapearMatricula(resultado));
            }
        }

        return matriculas;
    }

    public List<Matricula> listarPorAluno(Long alunoId) throws SQLException {

        String sql = SELECT_COMPLETO
                + " WHERE m.aluno_id = ? ORDER BY m.data_inicio DESC";

        List<Matricula> matriculas = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, alunoId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    matriculas.add(mapearMatricula(resultado));
                }
            }
        }

        return matriculas;
    }

    public boolean atualizar(Matricula matricula) throws SQLException {

        if (matricula.getId() == null) {
            throw new IllegalArgumentException(
                    "O ID da matrícula é obrigatório para atualização."
            );
        }

        String sql = """
                UPDATE matriculas
                SET numero_matricula = ?, aluno_id = ?, plano_id = ?,
                    data_inicio = ?, data_fim = ?, ativa = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, matricula.getNumeroMatricula());
            comando.setLong(2, matricula.getAluno().getId());
            comando.setLong(3, matricula.getPlano().getId());
            comando.setDate(4, Date.valueOf(matricula.getDataInicio()));
            comando.setDate(5, Date.valueOf(matricula.getDataFim()));
            comando.setBoolean(6, matricula.isAtiva());
            comando.setLong(7, matricula.getId());

            return comando.executeUpdate() > 0;
        }
    }

    public boolean desativar(Long id) throws SQLException {

        String sql = "UPDATE matriculas SET ativa = FALSE WHERE id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            return comando.executeUpdate() > 0;
        }
    }

    private Matricula mapearMatricula(ResultSet resultado)
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

        return new Matricula(
                resultado.getLong("matricula_id"),
                resultado.getString("numero_matricula"),
                aluno,
                plano,
                resultado.getDate("data_inicio").toLocalDate(),
                resultado.getDate("data_fim").toLocalDate(),
                resultado.getBoolean("ativa")
        );
    }
}