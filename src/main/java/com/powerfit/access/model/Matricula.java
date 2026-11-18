package com.powerfit.access.model;

import java.time.LocalDate;

public class Matricula {

    private Long id;
    private Aluno aluno;
    private Plano plano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;

    // Construtor completo
    public Matricula(Long id, Aluno aluno, Plano plano,
                     LocalDate dataInicio, LocalDate dataFim, boolean ativa) {
        this.id = id;
        this.aluno = aluno;
        this.plano = plano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = ativa;
    }

    // Construtor para nova matrícula
    public Matricula(Aluno aluno, Plano plano,
                     LocalDate dataInicio, LocalDate dataFim) {
        this(null, aluno, plano, dataInicio, dataFim, true);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Plano getPlano() {
        return plano;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public boolean isAtiva() {
        return ativa;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    // Controle da situação da matrícula
    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }
}