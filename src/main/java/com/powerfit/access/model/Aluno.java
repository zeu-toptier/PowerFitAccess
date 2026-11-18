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
        this.telefone = telefone;
        this.email = email;
        this.ativo = ativo;
        setDataCadastro(dataCadastro);
    }

    public Aluno(String nome, String cpf, String telefone, String email) {
        this(null, nome, cpf, telefone, email, true, LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF do aluno é obrigatório.");
        }
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        if (dataCadastro == null) {
            throw new IllegalArgumentException("A data de cadastro é obrigatória.");
        }
        this.dataCadastro = dataCadastro;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}