USE powerfit_access;

SET NAMES utf8mb4;

START TRANSACTION;

-- ==========================================
-- 1. CADASTRO DOS SETE ALUNOS FICTÍCIOS
-- ==========================================

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Caroline de Farias', '70000000001',
       '11990000001', 'caroline.farias@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000001'
       OR nome = 'Caroline de Farias'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Diego Silva de Freitas', '70000000002',
       '11990000002', 'diego.freitas@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000002'
       OR nome = 'Diego Silva de Freitas'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Elizeu Ribeiro de Oliveira', '70000000003',
       '11990000003', 'elizeu.ribeiro@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000003'
       OR nome = 'Elizeu Ribeiro de Oliveira'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Gabriella Galdino Araújo', '70000000004',
       '11990000004', 'gabriella.araujo@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000004'
       OR nome = 'Gabriella Galdino Araújo'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'João Victor de Oliveira Nunes Casagrande', '70000000005',
       '11990000005', 'joao.casagrande@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000005'
       OR nome = 'João Victor de Oliveira Nunes Casagrande'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Lucas Silveira do Nascimento', '70000000006',
       '11990000006', 'lucas.nascimento@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000006'
       OR nome = 'Lucas Silveira do Nascimento'
);

INSERT INTO alunos
(nome, cpf, telefone, email, ativo, data_cadastro)
SELECT 'Sana Soares Hachem', '70000000007',
       '11990000007', 'sana.hachem@example.com',
       TRUE, CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE cpf = '70000000007'
       OR nome = 'Sana Soares Hachem'
);

-- ==========================================
-- 2. PLANOS FICTÍCIOS
-- ==========================================

INSERT INTO planos (nome, descricao, valor_mensal, duracao_dias, ativo)
SELECT 'Plano Básico', 'Plano demonstrativo básico',
       89.90, 30, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM planos WHERE nome = 'Plano Básico'
);

INSERT INTO planos (nome, descricao, valor_mensal, duracao_dias, ativo)
SELECT 'Plano Plus', 'Plano demonstrativo intermediário',
       129.90, 90, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM planos WHERE nome = 'Plano Plus'
);

INSERT INTO planos (nome, descricao, valor_mensal, duracao_dias, ativo)
SELECT 'Plano Premium', 'Plano demonstrativo completo',
       179.90, 365, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM planos WHERE nome = 'Plano Premium'
);

-- ==========================================
-- 3. MATRÍCULAS INDIVIDUAIS
-- ==========================================

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-001', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Básico'
WHERE a.nome = 'Caroline de Farias'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-001'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-002', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 90 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Plus'
WHERE a.nome = 'Diego Silva de Freitas'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-002'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-003', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 365 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Premium'
WHERE a.nome = 'Elizeu Ribeiro de Oliveira'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-003'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-004', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Básico'
WHERE a.nome = 'Gabriella Galdino Araújo'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-004'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-005', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 90 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Plus'
WHERE a.nome = 'João Victor de Oliveira Nunes Casagrande'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-005'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-006', a.id, p.id,
       DATE_SUB(CURRENT_DATE, INTERVAL 60 DAY),
       DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Básico'
WHERE a.nome = 'Lucas Silveira do Nascimento'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-006'
);

INSERT INTO matriculas
(numero_matricula, aluno_id, plano_id, data_inicio, data_fim, ativa)
SELECT 'PFA-2026-007', a.id, p.id,
       CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 365 DAY), TRUE
FROM alunos a
JOIN planos p ON p.nome = 'Plano Premium'
WHERE a.nome = 'Sana Soares Hachem'
AND NOT EXISTS (
    SELECT 1 FROM matriculas WHERE numero_matricula = 'PFA-2026-007'
);

-- ==========================================
-- 4. PAGAMENTOS DEMONSTRATIVOS
-- ==========================================

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, CURRENT_DATE, CURRENT_DATE, 'PAGO'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-001'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, NULL,
       DATE_ADD(CURRENT_DATE, INTERVAL 10 DAY), 'PENDENTE'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-002'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, CURRENT_DATE, CURRENT_DATE, 'PAGO'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-003'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, NULL,
       DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY), 'ATRASADO'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-004'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, CURRENT_DATE, CURRENT_DATE, 'PAGO'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-005'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, CURRENT_DATE, CURRENT_DATE, 'PAGO'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-006'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

INSERT INTO pagamentos
(matricula_id, valor, data_pagamento, data_vencimento, status)
SELECT m.id, p.valor_mensal, NULL,
       DATE_ADD(CURRENT_DATE, INTERVAL 10 DAY), 'PENDENTE'
FROM matriculas m
JOIN planos p ON p.id = m.plano_id
WHERE m.numero_matricula = 'PFA-2026-007'
AND NOT EXISTS (
    SELECT 1 FROM pagamentos WHERE matricula_id = m.id
);

COMMIT;

-- ==========================================
-- 5. CONFERÊNCIA DOS CADASTROS
-- ==========================================

SELECT
    a.id,
    a.nome,
    a.cpf,
    a.telefone,
    a.email,
    m.numero_matricula,
    p.nome AS plano,
    pg.status AS situacao_pagamento,
    m.data_fim
FROM alunos a
LEFT JOIN matriculas m ON m.aluno_id = a.id
LEFT JOIN planos p ON p.id = m.plano_id
LEFT JOIN pagamentos pg ON pg.matricula_id = m.id
WHERE a.nome IN (
    'Caroline de Farias',
    'Diego Silva de Freitas',
    'Elizeu Ribeiro de Oliveira',
    'Gabriella Galdino Araújo',
    'João Victor de Oliveira Nunes Casagrande',
    'Lucas Silveira do Nascimento',
    'Sana Soares Hachem'
)
ORDER BY a.nome;
