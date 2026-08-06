package com.pacto.recrutamento.repository;

import com.pacto.recrutamento.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    List<Candidatura> findByCandidatoId(Long candidatoId);

    List<Candidatura> findByVagaId(Long vagaId);

    Optional<Candidatura> findByVagaIdAndCandidatoId(Long vagaId, Long candidatoId);

    boolean existsByVagaIdAndCandidatoId(Long vagaId, Long candidatoId);
}