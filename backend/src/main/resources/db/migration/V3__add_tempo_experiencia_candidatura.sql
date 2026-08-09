-- V3__add_tempo_experiencia_candidatura.sql
-- Adiciona o tempo de experiencia (em anos) informado pelo candidato

ALTER TABLE candidatura
    ADD COLUMN tempo_experiencia_anos INTEGER;