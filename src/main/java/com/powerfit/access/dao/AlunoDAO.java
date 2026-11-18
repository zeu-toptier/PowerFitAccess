package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO {

    public void cadastrar(Aluno aluno) throws SQLException {

        String sql = """
                INSERT INTO alunos
                (nome, cpf, telefone, email, ativo, data_cadastro)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, aluno.getNome());
            comando.setString(2, aluno.getCpf());
            comando.setString(3, aluno.getTelefone());
            comando.setString(4, aluno.getEmail());
            comando.setBoolean(5, aluno.isAtivo());
            comando.setDate(6, Date.valueOf(aluno.getDataCadastro()));

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    aluno.setId(resultado.getLong(1));
                }
            }
        }
    }

    public Optional<Aluno> buscarPorId(Long id) throws SQLException {

        String sql = "SELECT * FROM alunos WHERE id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearAluno(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Aluno> buscarPorCpf(String cpf) throws SQLException {

        String sql = "SELECT * FROM alunos WHERE cpf = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, cpf);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearAluno(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public List<Aluno> listarTodos() throws SQLException {

        String sql = "SELECT * FROM alunos ORDER BY nome";

        List<Aluno> alunos = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                alunos.add(mapearAluno(resultado));
            }
        }

        return alunos;
    }

    public boolean atualizar(Aluno aluno) throws SQLException {

        if (aluno.getId() == null) {
            throw new IllegalArgumentException(
                    "O ID do aluno é obrigatório para atualização."
            );
        }

        String sql = """
                UPDATE alunos
                SET nome = ?, cpf = ?, telefone = ?, email = ?,
                    ativo = ?, data_cadastro = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getNome());
            comando.setString(2, aluno.getCpf());
            comando.setString(3, aluno.getTelefone());
            comando.setString(4, aluno.getEmail());
            comando.setBoolean(5, aluno.isAtivo());
            comando.setDate(6, Date.valueOf(aluno.getDataCadastro()));
            comando.setLong(7, aluno.getId());

            return comando.executeUpdate() > 0;
        }
    }

    public boolean desativar(Long id) throws SQLException {

        String sql = "UPDATE alunos SET ativo = FALSE WHERE id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            return comando.executeUpdate() > 0;
        }
    }

    private Aluno mapearAluno(ResultSet resultado) throws SQLException {

        return new Aluno(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getString("cpf"),
                resultado.getString("telefone"),
                resultado.getString("email"),
                resultado.getBoolean("ativo"),
                resultado.getDate("data_cadastro").toLocalDate()
        );
    }
}