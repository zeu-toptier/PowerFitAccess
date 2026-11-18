package com.powerfit.access.dao;

import com.powerfit.access.database.ConexaoBanco;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.ResultadoAcesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroAcessoDAO {

    private static final String SELECT_COMPLETO = """
            SELECT
                r.id AS registro_id,
                r.aluno_id,
                r.numero_matricula,
                r.data_hora,
                r.resultado,
                r.motivo,
                a.id AS aluno_id_resultado,
                a.nome AS aluno_nome,
                a.cpf AS aluno_cpf,
                a.telefone AS aluno_telefone,
                a.email AS aluno_email,
                a.ativo AS aluno_ativo,
                a.data_cadastro AS aluno_data_cadastro
            FROM registros_acesso r
            LEFT JOIN alunos a ON r.aluno_id = a.id
            """;

    public void registrar(RegistroAcesso registro) throws SQLException {

        String sql = """
                INSERT INTO registros_acesso
                (aluno_id, numero_matricula, data_hora, resultado, motivo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            if (registro.getAluno() == null) {
                comando.setNull(1, java.sql.Types.BIGINT);
            } else {
                comando.setLong(1, registro.getAluno().getId());
            }

            comando.setString(2, registro.getNumeroMatricula());
            comando.setTimestamp(3, Timestamp.valueOf(registro.getDataHora()));
            comando.setString(4, registro.getResultado().name());
            comando.setString(5, registro.getMotivo());

            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    registro.setId(chaves.getLong(1));
                }
            }
        }
    }

    public Optional<RegistroAcesso> buscarPorId(Long id) throws SQLException {

        String sql = SELECT_COMPLETO + " WHERE r.id = ?";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearRegistro(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public List<RegistroAcesso> listarTodos() throws SQLException {

        String sql = SELECT_COMPLETO + " ORDER BY r.data_hora DESC";

        return executarConsulta(sql);
    }

    public List<RegistroAcesso> listarPorAluno(Long alunoId)
            throws SQLException {

        String sql = SELECT_COMPLETO
                + " WHERE r.aluno_id = ? ORDER BY r.data_hora DESC";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setLong(1, alunoId);

            try (ResultSet resultado = comando.executeQuery()) {
                return mapearLista(resultado);
            }
        }
    }

    public List<RegistroAcesso> listarPorResultado(ResultadoAcesso tipo)
            throws SQLException {

        String sql = SELECT_COMPLETO
                + " WHERE r.resultado = ? ORDER BY r.data_hora DESC";

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, tipo.name());

            try (ResultSet resultado = comando.executeQuery()) {
                return mapearLista(resultado);
            }
        }
    }

    public List<RegistroAcesso> listarAutorizados() throws SQLException {
        return listarPorResultado(ResultadoAcesso.AUTORIZADO);
    }

    public List<RegistroAcesso> listarNegados() throws SQLException {
        return listarPorResultado(ResultadoAcesso.NEGADO);
    }

    private List<RegistroAcesso> executarConsulta(String sql)
            throws SQLException {

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            return mapearLista(resultado);
        }
    }

    private List<RegistroAcesso> mapearLista(ResultSet resultado)
            throws SQLException {

        List<RegistroAcesso> registros = new ArrayList<>();

        while (resultado.next()) {
            registros.add(mapearRegistro(resultado));
        }

        return registros;
    }

    private RegistroAcesso mapearRegistro(ResultSet resultado)
            throws SQLException {

        Aluno aluno = null;

        long alunoId = resultado.getLong("aluno_id_resultado");

        if (!resultado.wasNull()) {
            aluno = new Aluno(
                    alunoId,
                    resultado.getString("aluno_nome"),
                    resultado.getString("aluno_cpf"),
                    resultado.getString("aluno_telefone"),
                    resultado.getString("aluno_email"),
                    resultado.getBoolean("aluno_ativo"),
                    resultado.getDate("aluno_data_cadastro").toLocalDate()
            );
        }

        return new RegistroAcesso(
                resultado.getLong("registro_id"),
                aluno,
                resultado.getString("numero_matricula"),
                resultado.getTimestamp("data_hora").toLocalDateTime(),
                ResultadoAcesso.valueOf(resultado.getString("resultado")),
                resultado.getString("motivo")
        );
    }
}