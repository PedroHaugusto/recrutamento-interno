package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.VagaRequest;
import com.pacto.recrutamento.dto.VagaResponse;
import com.pacto.recrutamento.exception.ResourceNotFoundException;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.Vaga;
import com.pacto.recrutamento.model.enums.RoleUsuario;
import com.pacto.recrutamento.model.enums.StatusVaga;
import com.pacto.recrutamento.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private VagaService vagaService;

    private Usuario admin;

    @BeforeEach
    void setUp() {
        admin = Usuario.builder()
                .id(1L)
                .nome("Administrador")
                .email("admin@pacto.com")
                .role(RoleUsuario.ADMIN)
                .build();
    }

    @Test
    void criar_devePersistirVagaComStatusAbertaMesmoSeRequestPedirOutro() {
        // Mesmo que o request tente forcar um status diferente,
        // toda vaga nova deve nascer como ABERTA (regra de negocio).
        VagaRequest request = new VagaRequest();
        request.setTitulo("Desenvolvedor Backend");
        request.setDescricao("Vaga para dev Java");
        request.setStatus(StatusVaga.FECHADA); // tentativa de burlar

        when(vagaRepository.save(any(Vaga.class))).thenAnswer(inv -> inv.getArgument(0));

        VagaResponse response = vagaService.criar(request, admin);

        assertEquals(StatusVaga.ABERTA, response.getStatus());
        assertEquals("Desenvolvedor Backend", response.getTitulo());
        assertEquals("Administrador", response.getResponsavelNome());
        verify(vagaRepository).save(any(Vaga.class));
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoVagaNaoExiste() {
        when(vagaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vagaService.buscarPorId(99L));
    }

    @Test
    void atualizar_devePermitirAlterarStatusExplicitamente() {
        Vaga vagaExistente = Vaga.builder()
                .id(5L)
                .titulo("Vaga Antiga")
                .descricao("Descricao antiga")
                .status(StatusVaga.ABERTA)
                .responsavel(admin)
                .build();

        when(vagaRepository.findById(5L)).thenReturn(Optional.of(vagaExistente));
        when(vagaRepository.save(any(Vaga.class))).thenAnswer(inv -> inv.getArgument(0));

        VagaRequest request = new VagaRequest();
        request.setTitulo("Vaga Atualizada");
        request.setDescricao("Nova descricao");
        request.setStatus(StatusVaga.FECHADA);

        VagaResponse response = vagaService.atualizar(5L, request);

        assertEquals("Vaga Atualizada", response.getTitulo());
        assertEquals(StatusVaga.FECHADA, response.getStatus());
    }

    @Test
    void excluir_deveLancarExcecaoQuandoVagaNaoExiste() {
        when(vagaRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vagaService.excluir(42L));
        verify(vagaRepository, never()).delete(any());
    }
}