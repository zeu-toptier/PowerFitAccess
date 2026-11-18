package com.powerfit.access.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {

    private Long id;
    private Matricula matricula;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private LocalDate dataVencimento;
    private StatusPagamento status;

    // Construtor completo
    public Pagamento(Long id, Matricula matricula, BigDecimal valor,
                     LocalDate dataPagamento, LocalDate dataVencimento,
                     StatusPagamento status) {
        this.id = id;
        this.matricula = matricula;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }

    // Construtor para novo pagamento
    public Pagamento(Matricula matricula, BigDecimal valor,
                     LocalDate dataVencimento) {
        this(null, matricula, valor, null, dataVencimento,
             StatusPagamento.PENDENTE);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    // Controle do pagamento
    public void registrarPagamento() {
        this.dataPagamento = LocalDate.now();
        this.status = StatusPagamento.PAGO;
    }

    public void marcarComoAtrasado() {
        this.status = StatusPagamento.ATRASADO;
    }

    public void marcarComoPendente() {
        this.status = StatusPagamento.PENDENTE;
        this.dataPagamento = null;
    }
}