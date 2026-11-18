package com.powerfit.access.model;

import java.time.LocalDate;

public class Matricula {

    private Long id;
    private String numeroMatricula;
    private Aluno aluno;
    private Plano plano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;

    public Matricula(Long id, String numeroMatricula, Aluno aluno,
                     Plano plano, LocalDate dataInicio,
                     LocalDate dataFim, boolean ativa) {
        this.id = id;
        setNumeroMatricula(numeroMatricula);
        setAluno(aluno);
        setPlano(plano);
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        this.ativa = ativa;
    }

    public Matricula(String numeroMatricula, Aluno aluno, Plano plano,
                     LocalDate dataInicio, LocalDate dataFim) {
        this(null, numeroMatricula, aluno, plano,
             dataInicio, dataFim, true);
    }

    public Long getId() {
        return id;
    }

    public String getNumeroMatricula() {
        return numeroMatricula;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        if (numeroMatricula == null || numeroMatricula.isBlank()) {
            throw new IllegalArgumentException("O número de matrícula é obrigatório.");
        }
        this.numeroMatricula = numeroMatricula;
    }

    public void setAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("A matrícula deve possuir um aluno.");
        }
        this.aluno = aluno;
    }

    public void setPlano(Plano plano) {
        if (plano == null) {
            throw new IllegalArgumentException("A matrícula deve possuir um plano.");
        }
        this.plano = plano;
    }

    public void setDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("A data de início é obrigatória.");
        }

        if (this.dataFim != null && dataFimAnterior(dataInicio, this.dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }

        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        if (dataFim == null) {
            throw new IllegalArgumentException("A data de término é obrigatória.");
        }

        if (this.dataInicio != null && dataFim.isBefore(this.dataInicio)) {
            throw new IllegalArgumentException("A data de término não pode ser anterior à data de início.");
        }

        this.dataFim = dataFim;
    }

    private boolean dataFimAnterior(LocalDate inicio, LocalDate fim) {
        return fim.isBefore(inicio);
    }

    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }
}