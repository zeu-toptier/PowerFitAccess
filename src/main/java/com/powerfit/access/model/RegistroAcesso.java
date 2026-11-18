package com.powerfit.access.model;

import java.time.LocalDateTime;

public class RegistroAcesso {

    private Long id;
    private Aluno aluno;
    private String numeroMatricula;
    private LocalDateTime dataHora;
    private ResultadoAcesso resultado;
    private String motivo;

    // Construtor completo
    public RegistroAcesso(Long id, Aluno aluno, String numeroMatricula,
                          LocalDateTime dataHora, ResultadoAcesso resultado,
                          String motivo) {
        this.id = id;
        this.aluno = aluno;
        this.numeroMatricula = numeroMatricula;
        this.dataHora = dataHora;
        this.resultado = resultado;
        this.motivo = motivo;
    }

    // Construtor para novo registro
    public RegistroAcesso(Aluno aluno, String numeroMatricula,
                          ResultadoAcesso resultado, String motivo) {
        this(null, aluno, numeroMatricula, LocalDateTime.now(),
             resultado, motivo);
    }

    // Getters
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

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setResultado(ResultadoAcesso resultado) {
        this.resultado = resultado;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}