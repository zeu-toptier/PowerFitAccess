# POWER FIT ACCESS
## Documento de Arquitetura do Sistema

**Projeto:** Power Fit Gym
**Sistema:** Power Fit Access
**Linguagem:** Java 21
**Paradigma:** Programação Orientada a Objetos (POO)
**Banco de dados:** MySQL 8.4
**Acesso ao banco:** JDBC
**Gerenciador de dependências:** Maven

---

## 1. Visão geral

O Power Fit Access é um sistema acadêmico desenvolvido para gerenciar e controlar o acesso de alunos de uma academia por meio de uma catraca simulada.

O sistema será executado inicialmente pelo terminal, permitindo cadastrar alunos, consultar planos, gerenciar matrículas, registrar pagamentos e validar tentativas de acesso.

A catraca será simulada por software, sem conexão com equipamentos físicos.

## 2. Arquitetura do sistema

O projeto utilizará uma arquitetura em camadas, organizada por responsabilidades.

As principais camadas serão:

- **Model:** representação dos objetos e entidades do sistema.
- **DAO:** comunicação com o banco de dados.
- **Service:** implementação das regras de negócio.
- **Database:** gerenciamento da conexão com o MySQL.
- **View:** interação com o usuário.

Essa organização busca facilitar a manutenção, os testes e a evolução do sistema.

## 3. Estrutura de pacotes

```text
com.powerfit.access
│
├── Principal.java
│
├── model/
│   ├── Aluno.java
│   ├── Plano.java
│   ├── Matricula.java
│   ├── Pagamento.java
│   └── RegistroAcesso.java
│
├── dao/
│   ├── AlunoDAO.java
│   ├── PlanoDAO.java
│   ├── MatriculaDAO.java
│   ├── PagamentoDAO.java
│   └── RegistroAcessoDAO.java
│
├── service/
│   ├── ControleAcesso.java
│   └── Catraca.java
│
├── database/
│   └── ConexaoBanco.java
│
└── view/
    └── MenuPrincipal.java
```

A estrutura representa a organização planejada. Os arquivos Java serão implementados nas próximas etapas.

## 4. Responsabilidade das classes

### 4.1. Model

| Classe | Responsabilidade |
|---|---|
| Aluno | Representar os dados cadastrais do aluno. |
| Plano | Representar os planos oferecidos pela academia. |
| Matricula | Representar o vínculo entre aluno e plano. |
| Pagamento | Representar os registros de pagamento. |
| RegistroAcesso | Representar cada tentativa de entrada, autorizada ou negada. |

### 4.2. DAO

As classes DAO serão responsáveis pelas operações de persistência no MySQL.

| Classe | Responsabilidade |
|---|---|
| AlunoDAO | Cadastrar, consultar, atualizar e desativar alunos. |
| PlanoDAO | Cadastrar, consultar e atualizar planos. |
| MatriculaDAO | Gerenciar matrículas. |
| PagamentoDAO | Registrar e consultar pagamentos. |
| RegistroAcessoDAO | Armazenar e consultar tentativas de acesso. |

A desativação de um aluno deverá preservar seus registros históricos, evitando a exclusão definitiva de informações relacionadas.

### 4.3. Service

| Classe | Responsabilidade |
|---|---|
| ControleAcesso | Consultar os dados necessários e aplicar as regras de autorização e bloqueio. |
| Catraca | Simular a liberação ou o bloqueio da entrada. |

### 4.4. Database

| Classe | Responsabilidade |
|---|---|
| ConexaoBanco | Estabelecer e disponibilizar conexões JDBC com o MySQL. |

### 4.5. View

| Classe | Responsabilidade |
|---|---|
| MenuPrincipal | Apresentar as opções e receber as entradas do usuário. |

### 4.6. Classe principal

| Classe | Responsabilidade |
|---|---|
| Principal | Inicializar o sistema e conectar os componentes necessários. |

## 5. Regras de negócio do acesso

Para autorizar a entrada, o sistema deverá verificar:

1. Se o aluno está cadastrado e ativo.
2. Se existe uma matrícula válida vinculada ao aluno.
3. Se a matrícula está ativa.
4. Se o plano está dentro do período de validade.
5. Se a situação financeira permite o acesso, sem pagamentos pendentes que impeçam a entrada.

Se todas as condições forem atendidas, o acesso será autorizado.

Caso alguma condição não seja atendida, o acesso será negado e o motivo deverá ser informado.

A política de pendências financeiras deverá ser aplicada de maneira consistente durante a validação.

Todas as tentativas deverão ser registradas, inclusive quando o aluno não estiver cadastrado.

## 6. Fluxo de funcionamento

```text
INÍCIO
   |
   v
Informar número da matrícula
   |
   v
Consultar aluno no banco
   |
   v
Aluno cadastrado e ativo?
   |
   +---- NÃO ----> Definir motivo da negativa
   |
   SIM
   |
   v
Verificar matrícula e plano
   |
   v
Matrícula ativa e plano válido?
   |
   +---- NÃO ----> Definir motivo da negativa
   |
   SIM
   |
   v
Verificar situação financeira
   |
   v
Pagamento regular?
   |
   +---- NÃO ----> Definir motivo da negativa
   |
   SIM
   |
   v
Autorizar acesso
   |
   v
Simular abertura da catraca
   |
   v
Registrar resultado da tentativa
   |
   v
FIM
```

Independentemente do resultado das verificações, o sistema deverá registrar a tentativa de acesso.

O registro deverá conter, conforme os dados disponíveis:

- Identificação do aluno, quando localizado.
- Número de matrícula informado.
- Data e horário da tentativa.
- Resultado: autorizado ou negado.
- Motivo da negativa, quando aplicável.

O registro deverá ocorrer tanto nas tentativas autorizadas quanto nas negadas.

## 7. Estrutura prevista do banco de dados

O banco de dados será denominado `powerfit_access`.

| Tabela | Finalidade |
|---|---|
| alunos | Armazenar os dados dos alunos. |
| planos | Armazenar os planos da academia. |
| matriculas | Relacionar alunos e planos. |
| pagamentos | Armazenar informações de pagamentos. |
| registros_acesso | Registrar as tentativas de entrada. |

As tabelas terão relacionamentos por meio de chaves primárias e estrangeiras.

O detalhamento dos campos, das restrições e dos relacionamentos será desenvolvido na documentação específica do banco de dados.

## 8. Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Desenvolvimento da aplicação. |
| POO | Organização do código em objetos e classes. |
| JDBC | Comunicação com o banco de dados. |
| MySQL 8.4 | Persistência dos dados. |
| Maven | Gerenciamento de dependências e compilação. |
| JUnit 5 | Testes automatizados. |
| Docker | Execução do banco MySQL em contêiner. |
| Git e GitHub | Controle de versão e armazenamento do projeto. |

## 9. Limites do projeto

Nesta versão acadêmica:

- Não haverá API REST.
- Não será utilizado Spring Boot.
- Não será utilizado JPA ou Hibernate.
- Não haverá integração com catraca física.
- Não haverá processamento financeiro real.
- A interface inicial será executada pelo terminal.

## 10. Evolução prevista

Após a conclusão da versão inicial, poderão ser avaliadas melhorias como:

- Interface gráfica com Java Swing.
- Relatórios de acesso.
- Consultas administrativas.
- Melhorias na experiência do usuário.

Essas funcionalidades dependerão da conclusão e avaliação da versão inicial.

---

**Power Fit Access — Documento de Arquitetura**
**Projeto acadêmico — Fatec de Mauá**