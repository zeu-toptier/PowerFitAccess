package com.powerfit.access.model;

import java.math.BigDecimal;

public class Plano {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valorMensal;
    private int duracaoDias;
    private boolean ativo;

    public Plano(Long id, String nome, String descricao,
                 BigDecimal valorMensal, int duracaoDias, boolean ativo) {
        this.id = id;
        setNome(nome);
        this.descricao = descricao;
        setValorMensal(valorMensal);
        setDuracaoDias(duracaoDias);
        this.ativo = ativo;
    }

    public Plano(String nome, String descricao,
                 BigDecimal valorMensal, int duracaoDias) {
        this(null, nome, descricao, valorMensal, duracaoDias, true);
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do plano é obrigatório.");
        }
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        if (valorMensal == null || valorMensal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do plano deve ser maior que zero.");
        }
        this.valorMensal = valorMensal;
    }

    public void setDuracaoDias(int duracaoDias) {
        if (duracaoDias <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }
        this.duracaoDias = duracaoDias;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}