package com.pacto.recrutamento.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatorio")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String nome;

    @NotBlank(message = "E-mail é obrigatorio")
    @Email(message = "E-mail invalido")
    private String email;

    @NotBlank(message = "Senha é obrigatoria")
    @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
    private String senha;
}