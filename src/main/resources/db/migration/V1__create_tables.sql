-- ====================================================================
-- 1. CRIANDO AS TABELAS BASE (SEM CHAVES ESTRANGEIRAS)
-- ====================================================================

create table perfil (
                        id_perfil serial primary key,
                        cargo varchar(50) not null,
                        perm_banco_dados boolean default false
);

create table categoria (
                           id_categoria serial primary key,
                           descricao varchar(50) not null
);

create table genero (
                        id_genero serial primary key,
                        genero varchar(50) not null,
                        descricao varchar(100)
);

create table sala (
                      id_sala serial primary key,
                      ativo boolean default true
);

create table cliente (
                         id_cliente serial primary key,
                         nome varchar(100) not null,
                         estudante boolean default false,
                         saldo double precision default 0.0,
                         ativo boolean default true,
                         login varchar(50) not null unique,
                         senha char(60) not null -- aumentado para 60 para suportar bcrypt/criptografia do java
);

-- ====================================================================
-- 2. CRIANDO AS TABELAS INTERMEDIÁRIAS E DEPENDENTES
-- ====================================================================

create table funcionario (
                             id_funcionario serial primary key,
                             id_perfil int not null,
                             nome varchar(100) not null,
                             comissao double precision default 0.0,
                             ativo boolean default true,
                             login varchar(50) not null unique,
                             senha char(60) not null,
                             constraint fk_funcionario_perfil foreign key (id_perfil) references perfil(id_perfil)
);

create table produto (
                         id_produto serial primary key,
                         id_categoria int not null,
                         descricao varchar(50) not null,
                         valor double precision not null,
                         quantidade int not null default 0,
                         imposto double precision default 0.0,
                         constraint fk_produto_categoria foreign key (id_categoria) references categoria(id_categoria)
);

create table filme (
                       id_filme serial primary key,
                       id_genero int not null,
                       titulo varchar(100) not null,
                       duracao int not null, -- em minutos
                       exibicao char(2) not null, -- ex: 2d, 3d
                       ativo boolean default true,
                       constraint fk_filme_genero foreign key (id_genero) references genero(id_genero)
);

-- ====================================================================
-- 3. CRIANDO AS TABELAS DO FLUXO DE MOVIMENTAÇÃO E COMPRAS
-- ====================================================================

create table sessao (
                        id_sessao serial primary key,
                        id_filme int not null,
                        id_sala int not null,
                        data_hora timestamp not null, -- timestamp guarda data e hora
                        capacidade int not null,
                        constraint fk_sessao_filme foreign key (id_filme) references filme(id_filme),
                        constraint fk_sessao_sala foreign key (id_sala) references sala(id_sala)
);

create table avaliacao (
                           id_avaliacao serial primary key,
                           id_cliente int not null,
                           id_filme int not null,
                           nota double precision not null,
                           comentario varchar(500),
                           data_hora timestamp not null,
                           constraint fk_avaliacao_cliente foreign key (id_cliente) references cliente(id_cliente),
                           constraint fk_avaliacao_filme foreign key (id_filme) references filme(id_filme)
);

create table compra (
                        id_compra serial primary key,
                        id_cliente int not null,
                        id_funcionario int, -- pode ser nulo se a compra for online pelo site
                        data_hora timestamp not null default current_timestamp,
                        valor_total double precision not null default 0.0,
                        constraint fk_compra_cliente foreign key (id_cliente) references cliente(id_cliente),
                        constraint fk_compra_funcionario foreign key (id_funcionario) references funcionario(id_funcionario)
);

create table item_compra (
                            id_item_compra serial primary key,
                            id_compra int not null,
                            id_produto int not null,
                            descricao varchar(50) not null,
                            quantidade int not null,
                            valor double precision not null,
                            imposto double precision default 0.0,
                            constraint fk_item_compra_compra foreign key (id_compra) references compra(id_compra) on delete cascade,
                            constraint fk_item_compra_produto foreign key (id_produto) references produto(id_produto)
);

create table ingresso (
                          id_ingresso serial primary key,
                          id_item_compra int not null,
                          id_sessao int not null,
                          tipo_ingresso varchar(50) not null, -- ex: inteira, meia
                          constraint fk_ingresso_item_compra foreign key (id_item_compra) references item_compra(id_item_compra) on delete cascade,
                          constraint fk_ingresso_sessao foreign key (id_sessao) references sessao(id_sessao)
);