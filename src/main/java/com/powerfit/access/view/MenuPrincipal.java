package com.powerfit.access.view;

import com.powerfit.access.dao.AlunoDAO;
import com.powerfit.access.dao.PlanoDAO;
import com.powerfit.access.dao.MatriculaDAO;
import com.powerfit.access.dao.PagamentoDAO;
import com.powerfit.access.dao.RegistroAcessoDAO;
import com.powerfit.access.model.Aluno;
import com.powerfit.access.model.Plano;
import com.powerfit.access.model.Matricula;
import com.powerfit.access.model.Pagamento;
import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.StatusPagamento;
import com.powerfit.access.service.Catraca;
import com.powerfit.access.service.ControleAcesso;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PlanoDAO planoDAO = new PlanoDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private final RegistroAcessoDAO registroAcessoDAO =
            new RegistroAcessoDAO();

    private final ControleAcesso controleAcesso = new ControleAcesso();
    private final Catraca catraca = new Catraca();

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void iniciar() {

        boolean executando = true;

        System.out.println("==================================");
        System.out.println("       POWER FIT ACCESS");
        System.out.println("==================================");

        while (executando) {
            System.out.println();
            System.out.println("========== MENU PRINCIPAL ==========");
            System.out.println("1 - Gerenciar alunos");
            System.out.println("2 - Gerenciar planos");
            System.out.println("3 - Gerenciar matrículas");
            System.out.println("4 - Gerenciar pagamentos");
            System.out.println("5 - Verificar acesso");
            System.out.println("6 - Consultar histórico de acessos");
            System.out.println("0 - Sair");

            String opcao = lerTexto("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case "1" -> menuAlunos();
                    case "2" -> menuPlanos();
                    case "3" -> menuMatriculas();
                    case "4" -> menuPagamentos();
                    case "5" -> verificarAcesso();
                    case "6" -> consultarHistorico();
                    case "0" -> {
                        executando = false;
                        System.out.println("Sistema encerrado.");
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao acessar o banco de dados: "
                        + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    // ==================== ALUNOS ====================

    private void menuAlunos() throws SQLException {

        boolean voltar = false;

        while (!voltar) {
            System.out.println();
            System.out.println("========== ALUNOS ==========");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Listar alunos");
            System.out.println("3 - Buscar aluno por CPF");
            System.out.println("4 - Atualizar aluno");
            System.out.println("5 - Desativar aluno");
            System.out.println("0 - Voltar");

            switch (lerTexto("Opção: ")) {
                case "1" -> cadastrarAluno();
                case "2" -> listarAlunos();
                case "3" -> buscarAlunoPorCpf();
                case "4" -> atualizarAluno();
                case "5" -> desativarAluno();
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarAluno() throws SQLException {

        System.out.println("\n--- CADASTRO DE ALUNO ---");

        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        String telefone = lerTexto("Telefone: ");
        String email = lerTexto("E-mail: ");

        Aluno aluno = new Aluno(nome, cpf, telefone, email);
        alunoDAO.cadastrar(aluno);

        System.out.println("Aluno cadastrado com sucesso.");
    }

    private void listarAlunos() throws SQLException {

        List<Aluno> alunos = alunoDAO.listarTodos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        System.out.println("\n--- LISTA DE ALUNOS ---");

        for (Aluno aluno : alunos) {
            exibirAluno(aluno);
        }
    }

    private void buscarAlunoPorCpf() throws SQLException {

        String cpf = lerTexto("Informe o CPF: ");
        Optional<Aluno> resultado = alunoDAO.buscarPorCpf(cpf);

        if (resultado.isPresent()) {
            exibirAluno(resultado.get());
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private void atualizarAluno() throws SQLException {

        Long id = lerLong("ID do aluno: ");
        Optional<Aluno> resultado = alunoDAO.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        Aluno aluno = resultado.get();

        aluno.setNome(lerTexto("Novo nome: "));
        aluno.setCpf(lerTexto("Novo CPF: "));
        aluno.setTelefone(lerTexto("Novo telefone: "));
        aluno.setEmail(lerTexto("Novo e-mail: "));

        if (alunoDAO.atualizar(aluno)) {
            System.out.println("Aluno atualizado com sucesso.");
        } else {
            System.out.println("Não foi possível atualizar o aluno.");
        }
    }

    private void desativarAluno() throws SQLException {

        Long id = lerLong("ID do aluno: ");

        if (alunoDAO.desativar(id)) {
            System.out.println("Aluno desativado com sucesso.");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private void exibirAluno(Aluno aluno) {

        System.out.println("-----------------------------------");
        System.out.println("ID: " + aluno.getId());
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("CPF: " + aluno.getCpf());
        System.out.println("Telefone: " + aluno.getTelefone());
        System.out.println("E-mail: " + aluno.getEmail());
        System.out.println("Ativo: " + aluno.isAtivo());
        System.out.println("Cadastro: "
                + aluno.getDataCadastro().format(FORMATO));
    }

    // ==================== PLANOS ====================

    private void menuPlanos() throws SQLException {

        boolean voltar = false;

        while (!voltar) {
            System.out.println();
            System.out.println("========== PLANOS ==========");
            System.out.println("1 - Cadastrar plano");
            System.out.println("2 - Listar planos");
            System.out.println("3 - Buscar plano por ID");
            System.out.println("4 - Atualizar plano");
            System.out.println("5 - Desativar plano");
            System.out.println("0 - Voltar");

            switch (lerTexto("Opção: ")) {
                case "1" -> cadastrarPlano();
                case "2" -> listarPlanos();
                case "3" -> buscarPlano();
                case "4" -> atualizarPlano();
                case "5" -> desativarPlano();
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarPlano() throws SQLException {

        System.out.println("\n--- CADASTRO DE PLANO ---");

        String nome = lerTexto("Nome: ");
        String descricao = lerTexto("Descrição: ");
        BigDecimal valor = lerDecimal("Valor mensal: ");
        int duracao = lerInteiro("Duração em dias: ");

        Plano plano = new Plano(nome, descricao, valor, duracao);
        planoDAO.cadastrar(plano);

        System.out.println("Plano cadastrado com sucesso.");
    }

    private void listarPlanos() throws SQLException {

        List<Plano> planos = planoDAO.listarTodos();

        if (planos.isEmpty()) {
            System.out.println("Nenhum plano cadastrado.");
            return;
        }

        System.out.println("\n--- LISTA DE PLANOS ---");

        for (Plano plano : planos) {
            exibirPlano(plano);
        }
    }

    private void buscarPlano() throws SQLException {

        Long id = lerLong("ID do plano: ");
        Optional<Plano> resultado = planoDAO.buscarPorId(id);

        if (resultado.isPresent()) {
            exibirPlano(resultado.get());
        } else {
            System.out.println("Plano não encontrado.");
        }
    }

    private void atualizarPlano() throws SQLException {

        Long id = lerLong("ID do plano: ");
        Optional<Plano> resultado = planoDAO.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("Plano não encontrado.");
            return;
        }

        Plano plano = resultado.get();

        plano.setNome(lerTexto("Novo nome: "));
        plano.setDescricao(lerTexto("Nova descrição: "));
        plano.setValorMensal(lerDecimal("Novo valor mensal: "));
        plano.setDuracaoDias(lerInteiro("Nova duração em dias: "));

        if (planoDAO.atualizar(plano)) {
            System.out.println("Plano atualizado com sucesso.");
        } else {
            System.out.println("Não foi possível atualizar o plano.");
        }
    }

    private void desativarPlano() throws SQLException {

        Long id = lerLong("ID do plano: ");

        if (planoDAO.desativar(id)) {
            System.out.println("Plano desativado com sucesso.");
        } else {
            System.out.println("Plano não encontrado.");
        }
    }

    private void exibirPlano(Plano plano) {

        System.out.println("-----------------------------------");
        System.out.println("ID: " + plano.getId());
        System.out.println("Nome: " + plano.getNome());
        System.out.println("Descrição: " + plano.getDescricao());
        System.out.println("Valor mensal: R$ " + plano.getValorMensal());
        System.out.println("Duração: " + plano.getDuracaoDias() + " dias");
        System.out.println("Ativo: " + plano.isAtivo());
    }

    // ==================== MATRÍCULAS ====================

    private void menuMatriculas() throws SQLException {

        boolean voltar = false;

        while (!voltar) {
            System.out.println();
            System.out.println("========== MATRÍCULAS ==========");
            System.out.println("1 - Cadastrar matrícula");
            System.out.println("2 - Listar matrículas");
            System.out.println("3 - Buscar por número");
            System.out.println("4 - Atualizar matrícula");
            System.out.println("5 - Desativar matrícula");
            System.out.println("0 - Voltar");

            switch (lerTexto("Opção: ")) {
                case "1" -> cadastrarMatricula();
                case "2" -> listarMatriculas();
                case "3" -> buscarMatricula();
                case "4" -> atualizarMatricula();
                case "5" -> desativarMatricula();
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarMatricula() throws SQLException {

        System.out.println("\n--- CADASTRO DE MATRÍCULA ---");

        String numero = lerTexto("Número da matrícula: ");
        Long alunoId = lerLong("ID do aluno: ");
        Long planoId = lerLong("ID do plano: ");

        Optional<Aluno> aluno = alunoDAO.buscarPorId(alunoId);
        Optional<Plano> plano = planoDAO.buscarPorId(planoId);

        if (aluno.isEmpty()) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        if (plano.isEmpty()) {
            System.out.println("Plano não encontrado.");
            return;
        }

        if (!aluno.get().isAtivo() || !plano.get().isAtivo()) {
            System.out.println("O aluno e o plano precisam estar ativos.");
            return;
        }

        LocalDate inicio = lerData("Data de início (dd/MM/aaaa): ");
        LocalDate fim = lerData("Data de término (dd/MM/aaaa): ");

        Matricula matricula = new Matricula(
                numero, aluno.get(), plano.get(), inicio, fim
        );

        matriculaDAO.cadastrar(matricula);

        System.out.println("Matrícula cadastrada com sucesso.");
    }

    private void listarMatriculas() throws SQLException {

        List<Matricula> matriculas = matriculaDAO.listarTodos();

        if (matriculas.isEmpty()) {
            System.out.println("Nenhuma matrícula cadastrada.");
            return;
        }

        System.out.println("\n--- LISTA DE MATRÍCULAS ---");

        for (Matricula matricula : matriculas) {
            exibirMatricula(matricula);
        }
    }

    private void buscarMatricula() throws SQLException {

        String numero = lerTexto("Número da matrícula: ");
        Optional<Matricula> resultado =
                matriculaDAO.buscarPorNumeroMatricula(numero);

        if (resultado.isPresent()) {
            exibirMatricula(resultado.get());
        } else {
            System.out.println("Matrícula não encontrada.");
        }
    }

    private void atualizarMatricula() throws SQLException {

        String numero = lerTexto("Número da matrícula: ");
        Optional<Matricula> resultado =
                matriculaDAO.buscarPorNumeroMatricula(numero);

        if (resultado.isEmpty()) {
            System.out.println("Matrícula não encontrada.");
            return;
        }

        Matricula matricula = resultado.get();

        Long alunoId = lerLong("Novo ID do aluno: ");
        Long planoId = lerLong("Novo ID do plano: ");

        Optional<Aluno> aluno = alunoDAO.buscarPorId(alunoId);
        Optional<Plano> plano = planoDAO.buscarPorId(planoId);

        if (aluno.isEmpty() || plano.isEmpty()) {
            System.out.println("Aluno ou plano não encontrado.");
            return;
        }

        if (!aluno.get().isAtivo() || !plano.get().isAtivo()) {
            System.out.println("O aluno e o plano precisam estar ativos.");
            return;
        }

        LocalDate novaDataInicio =
                lerData("Nova data de início (dd/MM/aaaa): ");
        LocalDate novaDataFim =
                lerData("Nova data de término (dd/MM/aaaa): ");

        matricula.setAluno(aluno.get());
        matricula.setPlano(plano.get());
        matricula.setPeriodo(novaDataInicio, novaDataFim);

        if (matriculaDAO.atualizar(matricula)) {
            System.out.println("Matrícula atualizada com sucesso.");
        } else {
            System.out.println("Não foi possível atualizar a matrícula.");
        }
    }

    private void desativarMatricula() throws SQLException {

        Long id = lerLong("ID da matrícula: ");

        if (matriculaDAO.desativar(id)) {
            System.out.println("Matrícula desativada com sucesso.");
        } else {
            System.out.println("Matrícula não encontrada.");
        }
    }

    private void exibirMatricula(Matricula matricula) {

        System.out.println("-----------------------------------");
        System.out.println("ID: " + matricula.getId());
        System.out.println("Número: " + matricula.getNumeroMatricula());
        System.out.println("Aluno: " + matricula.getAluno().getNome());
        System.out.println("Plano: " + matricula.getPlano().getNome());
        System.out.println("Início: "
                + matricula.getDataInicio().format(FORMATO));
        System.out.println("Término: "
                + matricula.getDataFim().format(FORMATO));
        System.out.println("Ativa: " + matricula.isAtiva());
    }

    // ==================== PAGAMENTOS ====================

    private void menuPagamentos() throws SQLException {

        boolean voltar = false;

        while (!voltar) {
            System.out.println();
            System.out.println("========== PAGAMENTOS ==========");
            System.out.println("1 - Registrar pagamento pendente");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Listar pendentes");
            System.out.println("4 - Listar atrasados");
            System.out.println("5 - Consultar por matrícula");
            System.out.println("6 - Marcar pagamento como pago");
            System.out.println("0 - Voltar");

            switch (lerTexto("Opção: ")) {
                case "1" -> cadastrarPagamento();
                case "2" -> listarPagamentos(pagamentoDAO.listarTodos());
                case "3" -> listarPagamentos(pagamentoDAO.listarPendentes());
                case "4" -> listarPagamentos(pagamentoDAO.listarAtrasados());
                case "5" -> listarPorMatricula();
                case "6" -> marcarPagamentoComoPago();
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarPagamento() throws SQLException {

        System.out.println("\n--- REGISTRO DE PAGAMENTO ---");

        String numero = lerTexto("Número da matrícula: ");
        Optional<Matricula> resultado =
                matriculaDAO.buscarPorNumeroMatricula(numero);

        if (resultado.isEmpty()) {
            System.out.println("Matrícula não encontrada.");
            return;
        }

        BigDecimal valor = lerDecimal("Valor: R$ ");
        LocalDate vencimento =
                lerData("Vencimento (dd/MM/aaaa): ");

        Pagamento pagamento = new Pagamento(
                resultado.get(), valor, vencimento
        );

        pagamentoDAO.cadastrar(pagamento);

        System.out.println("Pagamento pendente registrado com sucesso.");
    }

    private void listarPorMatricula() throws SQLException {

        Long id = lerLong("ID da matrícula: ");

        listarPagamentos(pagamentoDAO.listarPorMatricula(id));
    }

    private void marcarPagamentoComoPago() throws SQLException {

        Long id = lerLong("ID do pagamento: ");
        Optional<Pagamento> resultado = pagamentoDAO.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("Pagamento não encontrado.");
            return;
        }

        Pagamento pagamento = resultado.get();

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            System.out.println("Esse pagamento já está quitado.");
            return;
        }

        pagamento.registrarPagamento();

        if (pagamentoDAO.atualizar(pagamento)) {
            System.out.println("Pagamento marcado como pago.");
        } else {
            System.out.println("Não foi possível atualizar o pagamento.");
        }
    }

    private void listarPagamentos(List<Pagamento> pagamentos) {

        if (pagamentos.isEmpty()) {
            System.out.println("Nenhum pagamento encontrado.");
            return;
        }

        System.out.println("\n--- LISTA DE PAGAMENTOS ---");

        for (Pagamento pagamento : pagamentos) {
            System.out.println("-----------------------------------");
            System.out.println("ID: " + pagamento.getId());
            System.out.println("Matrícula: "
                    + pagamento.getMatricula().getNumeroMatricula());
            System.out.println("Aluno: "
                    + pagamento.getMatricula().getAluno().getNome());
            System.out.println("Valor: R$ " + pagamento.getValor());
            System.out.println("Vencimento: "
                    + pagamento.getDataVencimento().format(FORMATO));
            System.out.println("Status: " + pagamento.getStatus());

            if (pagamento.getDataPagamento() != null) {
                System.out.println("Pagamento realizado em: "
                        + pagamento.getDataPagamento().format(FORMATO));
            }
        }
    }

    // ==================== ACESSO E HISTÓRICO ====================

    private void verificarAcesso() throws SQLException {

        String numero =
                lerTexto("Informe o número da matrícula: ");

        RegistroAcesso registro =
                controleAcesso.verificarAcesso(numero);

        catraca.processar(registro);
    }

    private void consultarHistorico() throws SQLException {

        List<RegistroAcesso> registros =
                registroAcessoDAO.listarTodos();

        if (registros.isEmpty()) {
            System.out.println("Nenhum acesso registrado.");
            return;
        }

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println();
        System.out.println("======= HISTÓRICO DE ACESSOS =======");

        for (RegistroAcesso registro : registros) {
            System.out.println("-----------------------------------");
            System.out.println("Matrícula: "
                    + registro.getNumeroMatricula());
            System.out.println("Data: "
                    + registro.getDataHora().format(formato));
            System.out.println("Resultado: "
                    + registro.getResultado());
            System.out.println("Motivo: " + registro.getMotivo());
        }

        System.out.println("===================================");
    }

    // ==================== ENTRADA DE DADOS ====================

    private String lerTexto(String mensagem) {

        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private Long lerLong(String mensagem) {

        try {
            return Long.parseLong(lerTexto(mensagem));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Informe um número inteiro válido."
            );
        }
    }

    private int lerInteiro(String mensagem) {

        try {
            return Integer.parseInt(lerTexto(mensagem));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Informe um número inteiro válido."
            );
        }
    }

    private BigDecimal lerDecimal(String mensagem) {

        try {
            String valor = lerTexto(mensagem).replace(",", ".");
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Informe um valor monetário válido."
            );
        }
    }

    private LocalDate lerData(String mensagem) {

        try {
            return LocalDate.parse(
                    lerTexto(mensagem), FORMATO
            );
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Data inválida. Utilize o formato dd/MM/aaaa."
            );
        }
    }
}
