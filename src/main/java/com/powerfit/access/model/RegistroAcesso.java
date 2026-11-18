package com.powerfit.access.model;

import java.time.LocalDateTime;

public class RegistroAcesso {

    private Long id;
    private Aluno aluno;
    private String numeroMatricula;
    private LocalDateTime dataHora;
    private ResultadoAcesso resultado;
    private String motivo;

    public RegistroAcesso(Long id, Aluno aluno, String numeroMatricula,
                          LocalDateTime dataHora, ResultadoAcesso resultado,
                          String motivo) {
        this.id = id;
        this.aluno = aluno;
        setNumeroMatricula(numeroMatricula);
        setDataHora(dataHora);
        setResultado(resultado);
        setMotivo(motivo);
    }

    public RegistroAcesso(Aluno aluno, String numeroMatricula,
                          ResultadoAcesso resultado, String motivo) {
        this(null, aluno, numeroMatricula, LocalDateTime.now(),
             resultado, motivo);
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public String getNumeroMatricula() {
        return numeroMatricula;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public ResultadoAcesso getResultado() {
        return resultado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        if (numeroMatricula == null || numeroMatricula.isBlank()) {
            throw new IllegalArgumentException("O número de matrícula informado é obrigatório.");
        }
        this.numeroMatricula = numeroMatricula;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("A data e hora do registro são obrigatórias.");
        }
        this.dataHora = dataHora;
    }

    public void setResultado(ResultadoAcesso resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado do acesso é obrigatório.");
        }
        this.resultado = resultado;
    }

    public void setMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("O motivo do registro é obrigatório.");
        }
        this.motivo = motivo;
    }
}