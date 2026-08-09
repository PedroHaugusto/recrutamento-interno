package com.pacto.recrutamento.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CandidaturaRequest {

    @NotNull(message = "Tempo de experiência é obrigatório")
    @Min(value = 0, message = "Tempo de experiência não pode ser negativo")
    @Max(value = 60, message = "Tempo de experiência informado parece inválido")
    private Integer tempoExperienciaAnos;
}