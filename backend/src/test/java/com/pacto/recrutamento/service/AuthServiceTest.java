package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.AuthResponse;
import com.pacto.recrutamento.dto.LoginRequest;
import com.pacto.recrutamento.dto.RegisterRequest;
import com.pacto.recrutamento.exception.BusinessException;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.enums.RoleUsuario;
import com.pacto.recrutamento.repository.UsuarioRepository;
import com.pacto.recrutamento.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_deveLancarExcecaoQuandoEmailJaExiste() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Novo Usuario");
        request.setEmail("existente@pacto.com");
        request.setSenha("123456");

        when(usuarioRepository.existsByEmail("existente@pacto.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> authService.register(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void register_deveCriarUsuarioSempreComRoleCandidato() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Novo Usuario");
        request.setEmail("novo@pacto.com");
        request.setSenha("123456");

        when(usuarioRepository.existsByEmail("novo@pacto.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hash-fake");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("token-fake");

        AuthResponse response = authService.register(request);

        assertEquals(RoleUsuario.CANDIDATO, response.getRole());
        assertEquals("token-fake", response.getToken());
        assertEquals("novo@pacto.com", response.getEmail());
    }

    @Test
    void login_deveLancarExcecaoQuandoUsuarioNaoEncontradoAposAutenticar() {
        LoginRequest request = new LoginRequest();
        request.setEmail("teste@pacto.com");
        request.setSenha("123456");

        when(usuarioRepository.findByEmail("teste@pacto.com")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> authService.login(request));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_deveRetornarTokenQuandoCredenciaisValidas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@pacto.com");
        request.setSenha("admin123");

        Usuario admin = Usuario.builder()
                .id(1L)
                .nome("Administrador")
                .email("admin@pacto.com")
                .role(RoleUsuario.ADMIN)
                .build();

        when(usuarioRepository.findByEmail("admin@pacto.com")).thenReturn(Optional.of(admin));
        when(jwtService.generateToken(admin)).thenReturn("token-admin");

        AuthResponse response = authService.login(request);

        assertEquals("token-admin", response.getToken());
        assertEquals(RoleUsuario.ADMIN, response.getRole());
        verify(authenticationManager).authenticate(any());
    }
}