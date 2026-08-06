-- V1__create_tables.sql
-- Estrutura inicial: usuario, vaga, candidatura, notificacao

CREATE TYPE role_usuario AS ENUM ('ADMIN', 'CANDIDATO');
CREATE TYPE status_vaga AS ENUM ('ABERTA', 'FECHADA');
CREATE TYPE status_candidatura AS ENUM ('PENDENTE', 'EM_ANALISE', 'APROVADO', 'REJEITADO');

CREATE TABLE usuario (
                         id              BIGSERIAL PRIMARY KEY,
                         nome            VARCHAR(150) NOT NULL,
                         email           VARCHAR(150) NOT NULL UNIQUE,
                         senha_hash      VARCHAR(255) NOT NULL,
                         role            role_usuario NOT NULL,
                         data_criacao    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE vaga (
                      id              BIGSERIAL PRIMARY KEY,
                      titulo          VARCHAR(150) NOT NULL,
                      descricao       TEXT NOT NULL,
                      requisitos      TEXT,
                      status          status_vaga NOT NULL DEFAULT 'ABERTA',
                      responsavel_id  BIGINT NOT NULL REFERENCES usuario(id),
                      data_criacao    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_vaga_responsavel ON vaga(responsavel_id);
CREATE INDEX idx_vaga_status ON vaga(status);

CREATE TABLE candidatura (
                             id                  BIGSERIAL PRIMARY KEY,
                             vaga_id             BIGINT NOT NULL REFERENCES vaga(id),
                             candidato_id        BIGINT NOT NULL REFERENCES usuario(id),
                             status              status_candidatura NOT NULL DEFAULT 'PENDENTE',
                             data_candidatura    TIMESTAMP NOT NULL DEFAULT now(),
                             CONSTRAINT uk_candidatura_vaga_candidato UNIQUE (vaga_id, candidato_id)
);

CREATE INDEX idx_candidatura_vaga ON candidatura(vaga_id);
CREATE INDEX idx_candidatura_candidato ON candidatura(candidato_id);

CREATE TABLE notificacao (
                             id              BIGSERIAL PRIMARY KEY,
                             usuario_id      BIGINT NOT NULL REFERENCES usuario(id),
                             mensagem        VARCHAR(255) NOT NULL,
                             lida            BOOLEAN NOT NULL DEFAULT false,
                             data_criacao    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_notificacao_usuario ON notificacao(usuario_id);