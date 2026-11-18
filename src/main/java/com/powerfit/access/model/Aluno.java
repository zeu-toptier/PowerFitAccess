package com.powerfit.access.model;

import java.time.LocalDate;

public class Aluno {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private boolean ativo;
    private LocalDate dataCadastro;

    public Aluno(Long id, String nome, String cpf, String telefone,
                 String email, boolean ativo, LocalDate dataCadastro) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setEmail(email);
        this.ativo = ativo;

        if (dataCadastro == null) {
            throw new IllegalArgumentException(
                    "A data de cadastro é obrigatória."
            );
        }

        this.dataCadastro = dataCadastro;
    }

    public Aluno(String nome, String cpf, String telefone, String email) {
        this(null, nome, cpf, telefone, email, true, LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do aluno é obrigatório."
            );
        }

        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException(
                    "O CPF é obrigatório."
            );
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (!cpfLimpo.matches("\\d{11}")) {
            throw new IllegalArgumentException(
                    "CPF inválido. Informe exatamente 11 dígitos."
            );
        }

        this.cpf = cpfLimpo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            this.telefone = null;
            return;
        }

        String telefoneLimpo = telefone.replaceAll("\\D", "");

        if (!telefoneLimpo.matches("\\d{10,13}")) {
            throw new IllegalArgumentException(
                    "Telefone inválido. Informe entre 10 e 13 dígitos."
            );
        }

        this.telefone = telefoneLimpo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = null;
            return;
        }

        String emailLimpo = email.trim();

        if (!emailLimpo.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {
            throw new IllegalArgumentException(
                    "E-mail inválido. Informe um endereço válido."
            );
        }

        this.email = emailLimpo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        if (dataCadastro == null) {
            throw new IllegalArgumentException(
                    "A data de cadastro é obrigatória."
            );
        }

        this.dataCadastro = dataCadastro;
    }
}
