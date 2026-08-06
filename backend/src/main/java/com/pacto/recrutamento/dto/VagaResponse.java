package com.pacto.recrutamento.dto;

import com.pacto.recrutamento.model.enums.StatusVaga;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class VagaResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String requisitos;
    private StatusVaga status;
    private String responsavelNome;
    private LocalDateTime dataCriacao;
}