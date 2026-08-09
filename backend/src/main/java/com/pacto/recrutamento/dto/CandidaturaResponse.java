package com.pacto.recrutamento.dto;

import com.pacto.recrutamento.model.enums.StatusCandidatura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CandidaturaResponse {

    private Long id;
    private Long vagaId;
    private String vagaTitulo;
    private String candidatoNome;
    private StatusCandidatura status;
    private LocalDateTime dataCandidatura;
    private Integer tempoExperienciaAnos;
}