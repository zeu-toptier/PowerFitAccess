package com.powerfit.access.service;

import com.powerfit.access.dao.MatriculaDAO;
import com.powerfit.access.dao.PagamentoDAO;
import com.powerfit.access.dao.RegistroAcessoDAO;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Pagamento;
import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.ResultadoAcesso;
import com.powerfit.access.model.StatusPagamento;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ControleAcesso {

    private final MatriculaDAO matriculaDAO;
    private final PagamentoDAO pagamentoDAO;
    private final RegistroAcessoDAO registroAcessoDAO;

    public ControleAcesso() {
        this(new MatriculaDAO(), new PagamentoDAO(),
                new RegistroAcessoDAO());
    }

    public ControleAcesso(MatriculaDAO matriculaDAO,
                          PagamentoDAO pagamentoDAO,
                          RegistroAcessoDAO registroAcessoDAO) {
        this.matriculaDAO = matriculaDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.registroAcessoDAO = registroAcessoDAO;
    }

    public RegistroAcesso verificarAcesso(String numeroMatricula)
            throws SQLException {

        if (numeroMatricula == null || numeroMatricula.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe um número de matrícula válido."
            );
        }

        String numero = numeroMatricula.trim();

        Optional<Matricula> consulta =
                matriculaDAO.buscarPorNumeroMatricula(numero);

        if (consulta.isEmpty()) {
            return registrar(null, numero, ResultadoAcesso.NEGADO,
                    "Matrícula não encontrada.");
        }

        Matricula matricula = consulta.get();
        Aluno aluno = matricula.getAluno();

        if (!aluno.isAtivo()) {
            return registrar(aluno, numero, ResultadoAcesso.NEGADO,
                    "Aluno inativo.");
        }

        if (!matricula.isAtiva()) {
            return registrar(aluno, numero, ResultadoAcesso.NEGADO,
                    "Matrícula inativa.");
        }

        if (!matricula.getPlano().isAtivo()) {
            return registrar(aluno, numero, ResultadoAcesso.NEGADO,
                    "Plano inativo.");
        }

        LocalDate hoje = LocalDate.now();

        if (hoje.isBefore(matricula.getDataInicio())
                || hoje.isAfter(matricula.getDataFim())) {
            return registrar(aluno, numero, ResultadoAcesso.NEGADO,
                    "Matrícula fora do período de validade.");
        }

        List<Pagamento> pagamentos =
                pagamentoDAO.listarPorMatricula(matricula.getId());

        for (Pagamento pagamento : pagamentos) {

            if (pagamento.getStatus() == StatusPagamento.PENDENTE
                    && pagamento.getDataVencimento().isBefore(hoje)) {

                pagamento.marcarComoAtrasado();

                if (!pagamentoDAO.atualizar(pagamento)) {
                    throw new SQLException(
                            "Não foi possível atualizar o pagamento vencido."
                    );
                }
            }

            if (pagamento.getStatus() == StatusPagamento.PENDENTE
                    || pagamento.getStatus() == StatusPagamento.ATRASADO) {

                return registrar(aluno, numero, ResultadoAcesso.NEGADO,
                        "Existe pagamento pendente ou atrasado.");
            }
        }

        return registrar(aluno, numero, ResultadoAcesso.AUTORIZADO,
                "Acesso autorizado.");
    }

    private RegistroAcesso registrar(
            Aluno aluno,
            String numero,
            ResultadoAcesso resultado,
            String motivo) throws SQLException {

        RegistroAcesso registro = new RegistroAcesso(
                aluno, numero, resultado, motivo
        );

        registroAcessoDAO.registrar(registro);

        return registro;
    }
}
