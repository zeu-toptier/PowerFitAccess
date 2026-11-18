package com.powerfit.access.model;

import java.math.BigDecimal;

public class Plano {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valorMensal;
    private int duracaoDias;
    private boolean ativo;

    // Construtor completo
    public Plano(Long id, String nome, String descricao,
                 BigDecimal valorMensal, int duracaoDias, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorMensal = valorMensal;
        this.duracaoDias = duracaoDias;
        this.ativo = ativo;
    }

    // Construtor para novo plano
    public Plano(String nome, String descricao,
                 BigDecimal valorMensal, int duracaoDias) {
        this(null, nome, descricao, valorMensal, duracaoDias, true);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public int getDuracaoDias() {
        return duracaoDias;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public void setDuracaoDias(int duracaoDias) {
        this.duracaoDias = duracaoDias;
    }

    // Controle de situação do plano
    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}