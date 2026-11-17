# POWER FIT ACCESS

### Sistema de Gerenciamento e Controle de Acesso por Catraca

## Sobre o projeto

O **Power Fit Access** é um projeto acadêmico desenvolvido no contexto do Projeto de Extensão Comunitária da **Fatec de Mauá**, no curso Superior de Tecnologia em Informática para Negócios.

O sistema tem como objetivo desenvolver uma solução para o controle de acesso de alunos à academia fictícia Power Fit Gym, utilizando Java e Programação Orientada a Objetos (POO).

A aplicação realizará consultas cadastrais e verificações de matrícula, plano e pagamento para determinar se o acesso à academia poderá ser autorizado ou negado.

## Objetivo

Desenvolver um MVP (Produto Mínimo Viável) funcional para simular o controle de entrada de alunos por uma catraca, aplicando conceitos de programação orientada a objetos, persistência de dados e regras de negócio.

## Funcionalidades previstas

- Cadastro, consulta, atualização e exclusão de alunos.
- Cadastro e gerenciamento de planos.
- Criação e consulta de matrículas.
- Registro e consulta de pagamentos.
- Validação das condições de acesso dos alunos.
- Simulação de liberação e bloqueio da catraca.
- Registro de todas as tentativas de acesso.
- Consulta ao histórico de acessos autorizados e negados.

## Regras de acesso

O sistema deverá verificar as seguintes condições antes de autorizar uma entrada:

- O aluno deve estar cadastrado.
- A matrícula deve ser válida e estar ativa.
- O plano deve estar dentro do período de validade.
- O pagamento deve estar regularizado.

Caso alguma condição não seja atendida, o acesso será negado e o motivo ficará registrado no histórico.

## Tecnologias utilizadas ou previstas

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem principal |
| Programação Orientada a Objetos | Organização e estruturação do código |
| JDBC | Comunicação entre Java e MySQL |
| MySQL | Banco de dados relacional |
| MySQL Connector/J | Driver de conexão com o banco |
| Maven | Gerenciamento de dependências |
| JUnit | Testes automatizados |
| Git e GitHub | Versionamento e armazenamento do projeto |
| Visual Studio Code | Ambiente de desenvolvimento |
| GitHub Codespaces | Ambiente de desenvolvimento em nuvem |

## Estrutura de desenvolvimento

O sistema será organizado em classes e pacotes, separando as entidades, regras de negócio e operações de acesso ao banco de dados.

A aplicação utilizará Programação Orientada a Objetos, aplicando conceitos como abstração, encapsulamento, herança e polimorfismo quando adequados à estrutura do projeto.

A persistência das informações será realizada por meio de conexão JDBC com o banco de dados MySQL.

A aplicação será executada de forma independente, sem utilização de API REST nesta versão.

## Banco de dados

O banco de dados será responsável por armazenar e organizar as informações necessárias ao funcionamento do sistema.

As principais entidades previstas são:

- Alunos;
- Planos;
- Matrículas;
- Pagamentos;
- Registros de acesso.

## Testes

Serão realizados testes para verificar o funcionamento das operações de cadastro, consulta, atualização, integração com o banco de dados e validação de acesso.

Também serão testadas situações em que a entrada deverá ser autorizada ou negada, garantindo que todas as tentativas sejam registradas.

## Equipe do projeto

Integrantes em ordem alfabética:

1. Caroline de Farias
2. Diego Silva de Freitas
3. Elizeu Ribeiro de Oliveira
4. Gabriella Galdino Araújo
5. João Victor de Oliveira Nunes Casagrande
6. Lucas Silveira do Nascimento
7. Sana Soares Hachem

## Instituição

**Fatec de Mauá**

Curso Superior de Tecnologia em Informática para Negócios

Projeto de Extensão Comunitária — 3º Semestre de 2026.

## Finalidade acadêmica

Este repositório tem finalidade acadêmica e registra o desenvolvimento do Power Fit Access, demonstrando a aplicação prática de conceitos de programação, orientação a objetos, banco de dados e desenvolvimento de software.

---

**Power Fit Access — Power Fit Gym**