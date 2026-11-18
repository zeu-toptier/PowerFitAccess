package com.powerfit.access.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoBanco {

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(".")
            .ignoreIfMissing()
            .load();

    private static String obterVariavel(String nome) {
        String valor = DOTENV.get(nome);

        if (valor == null || valor.isBlank()) {
            valor = System.getenv(nome);
        }

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "A variável de ambiente " + nome + " não foi configurada."
            );
        }

        return valor;
    }

    public static Connection conectar() throws SQLException {

        String banco = obterVariavel("MYSQL_DATABASE");
        String usuario = obterVariavel("MYSQL_USER");
        String senha = obterVariavel("MYSQL_PASSWORD");

        String url = "jdbc:mysql://localhost:3306/" + banco
                + "?useSSL=false"
                + "&serverTimezone=America/Sao_Paulo"
                + "&allowPublicKeyRetrieval=true";

        return DriverManager.getConnection(url, usuario, senha);
    }
}