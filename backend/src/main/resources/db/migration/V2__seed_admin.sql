-- V2__seed_admin.sql
-- Cria um usuario administrador inicial, ja que o registro publico
-- so permite criar usuarios com role CANDIDATO.
--
-- Login: admin@pacto.com
-- Senha: admin123

INSERT INTO usuario (nome, email, senha_hash, role)
VALUES (
           'Administrador',
           'admin@pacto.com',
           '$2b$10$G6rQlnEuK7ltXYT3ybGMgeuTGjWZtbpJXsMIOnftoCK2uANOHcvya',
           'ADMIN'
       );