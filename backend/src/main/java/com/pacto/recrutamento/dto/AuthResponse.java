package com.pacto.recrutamento.dto;

import com.pacto.recrutamento.model.enums.RoleUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String nome;
    private String email;
    private RoleUsuario role;
}