package com.pacto.recrutamento.repository;

import com.pacto.recrutamento.model.Vaga;
import com.pacto.recrutamento.model.enums.StatusVaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByStatus(StatusVaga status);

    List<Vaga> findByResponsavelId(Long responsavelId);
}