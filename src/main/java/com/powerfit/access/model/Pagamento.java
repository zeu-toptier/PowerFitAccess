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

    public Pagamento(Long id, Matricula matricula, BigDecimal valor,
                     LocalDate dataPagamento, LocalDate dataVencimento,
                     StatusPagamento status) {
        this.id = id;
        setMatricula(matricula);
        setValor(valor);
        setDataVencimento(dataVencimento);

        if (status == null) {
            throw new IllegalArgumentException("O status do pagamento é obrigatório.");
        }

        if (status == StatusPagamento.PAGO && dataPagamento == null) {
            throw new IllegalArgumentException("Um pagamento pago deve possuir data de pagamento.");
        }

        if (status != StatusPagamento.PAGO && dataPagamento != null) {
            throw new IllegalArgumentException("Um pagamento não pago não deve possuir data de pagamento.");
        }

        this.dataPagamento = dataPagamento;
        this.status = status;
    }

    public Pagamento(Matricula matricula, BigDecimal valor,
                     LocalDate dataVencimento) {
        this(null, matricula, valor, null, dataVencimento,
             StatusPagamento.PENDENTE);
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("O pagamento deve estar associado a uma matrícula.");
        }
        this.matricula = matricula;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        if (dataVencimento == null) {
            throw new IllegalArgumentException("A data de vencimento é obrigatória.");
        }
        this.dataVencimento = dataVencimento;
    }

    public void registrarPagamento() {
        this.dataPagamento = LocalDate.now();
        this.status = StatusPagamento.PAGO;
    }

    public void marcarComoAtrasado() {
        if (this.status == StatusPagamento.PAGO) {
            throw new IllegalStateException("Um pagamento já realizado não pode ser marcado como atrasado.");
        }

        this.status = StatusPagamento.ATRASADO;
    }

    public void marcarComoPendente() {
        this.status = StatusPagamento.PENDENTE;
        this.dataPagamento = null;
    }
}