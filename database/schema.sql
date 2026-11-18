-- =====================================================
-- POWER FIT ACCESS
-- Estrutura inicial do banco de dados
-- MySQL 8.4
-- =====================================================

USE powerfit_access;

-- =====================================================
-- 1. TABELA: ALUNOS
-- =====================================================

CREATE TABLE IF NOT EXISTS alunos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(150),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 2. TABELA: PLANOS
-- =====================================================

CREATE TABLE IF NOT EXISTS planos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    valor_mensal DECIMAL(10,2) NOT NULL,
    duracao_dias INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_plano_valor
        CHECK (valor_mensal > 0),

    CONSTRAINT chk_plano_duracao
        CHECK (duracao_dias > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 3. TABELA: MATRICULAS
-- =====================================================

CREATE TABLE IF NOT EXISTS matriculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_matricula VARCHAR(30) NOT NULL UNIQUE,
    aluno_id BIGINT NOT NULL,
    plano_id BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_matricula_aluno
        FOREIGN KEY (aluno_id)
        REFERENCES alunos(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_matricula_plano
        FOREIGN KEY (plano_id)
        REFERENCES planos(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_matricula_datas
        CHECK (data_fim >= data_inicio),

    INDEX idx_matricula_aluno (aluno_id),
    INDEX idx_matricula_plano (plano_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 4. TABELA: PAGAMENTOS
-- =====================================================

CREATE TABLE IF NOT EXISTS pagamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_pagamento DATE,
    data_vencimento DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',

    CONSTRAINT fk_pagamento_matricula
        FOREIGN KEY (matricula_id)
        REFERENCES matriculas(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_pagamento_valor
        CHECK (valor > 0),

    CONSTRAINT chk_pagamento_status
        CHECK (status IN ('PENDENTE', 'PAGO', 'ATRASADO')),

    CONSTRAINT chk_pagamento_data
        CHECK (
            (status = 'PAGO' AND data_pagamento IS NOT NULL)
            OR
            (status IN ('PENDENTE', 'ATRASADO')
             AND data_pagamento IS NULL)
        ),

    INDEX idx_pagamento_matricula (matricula_id),
    INDEX idx_pagamento_status (status),
    INDEX idx_pagamento_vencimento (data_vencimento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 5. TABELA: REGISTROS DE ACESSO
-- =====================================================

CREATE TABLE IF NOT EXISTS registros_acesso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NULL,
    numero_matricula VARCHAR(30) NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resultado VARCHAR(20) NOT NULL,
    motivo VARCHAR(255) NOT NULL,

    CONSTRAINT fk_registro_aluno
        FOREIGN KEY (aluno_id)
        REFERENCES alunos(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_registro_resultado
        CHECK (resultado IN ('AUTORIZADO', 'NEGADO')),

    INDEX idx_registro_aluno (aluno_id),
    INDEX idx_registro_matricula (numero_matricula),
    INDEX idx_registro_data_hora (data_hora),
    INDEX idx_registro_resultado (resultado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;