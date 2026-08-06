package com.pacto.recrutamento.dto;

import com.pacto.recrutamento.model.enums.StatusVaga;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VagaRequest {

    @NotBlank(message = "Titulo e obrigatorio")
    @Size(max = 150, message = "Titulo deve ter no maximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "Descricao e obrigatoria")
    private String descricao;

    private String requisitos;

    private StatusVaga status;
}