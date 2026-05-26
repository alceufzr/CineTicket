-- ====================================================================
-- SCRIPT PARA APAGAR AS TABELAS (ORDEM INVERSA DE DEPENDÊNCIA)
-- ====================================================================

-- 1. Tabelas do Fluxo de Movimentação e Compras (Folhas da árvore de dependências)
drop table if exists ingresso;
drop table if exists item_compra;
drop table if exists compra;
drop table if exists avaliacao;
drop table if exists sessao;

-- 2. Tabelas Intermediárias e Dependentes
drop table if exists filme;
drop table if exists produto;
drop table if exists funcionario;

-- 3. Tabelas Base (Sem Chaves Estrangeiras)
drop table if exists cliente;
drop table if exists sala;
drop table if exists genero;
drop table if exists categoria;
drop table if exists perfil;