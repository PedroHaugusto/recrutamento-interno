package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.AuthResponse;
import com.pacto.recrutamento.dto.LoginRequest;
import com.pacto.recrutamento.dto.RegisterRequest;
import com.pacto.recrutamento.exception.BusinessException;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.enums.RoleUsuario;
import com.pacto.recrutamento.repository.UsuarioRepository;
import com.pacto.recrutamento.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senhaHash(passwordEncoder.encode(request.getSenha()))
                .role(RoleUsuario.CANDIDATO)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Usuario nao encontrado"));

        String token = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }
}