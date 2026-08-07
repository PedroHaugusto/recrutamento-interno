package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.CandidaturaResponse;
import com.pacto.recrutamento.exception.BusinessException;
import com.pacto.recrutamento.exception.ResourceNotFoundException;
import com.pacto.recrutamento.model.Candidatura;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.Vaga;
import com.pacto.recrutamento.model.enums.RoleUsuario;
import com.pacto.recrutamento.repository.CandidaturaRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidaturaServiceTest {

    @Mock
    private CandidaturaRepository candidaturaRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private CandidaturaService candidaturaService;

    private Usuario admin;
    private Usuario candidato;
    private Vaga vaga;

    @BeforeEach
    void setUp() {
        admin = Usuario.builder().id(1L).nome("Administrador").role(RoleUsuario.ADMIN).build();
        candidato = Usuario.builder().id(2L).nome("Joao Candidato").role(RoleUsuario.CANDIDATO).build();
        vaga = Vaga.builder().id(10L).titulo("Dev Backend").responsavel(admin).build();
    }

    @Test
    void candidatar_deveLancarExcecaoQuandoVagaNaoExiste() {
        when(vagaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> candidaturaService.candidatar(10L, candidato));

        verify(candidaturaRepository, never()).save(any());
        verify(notificacaoService, never()).notificar(any(), anyString());
    }

    @Test
    void candidatar_deveLancarExcecaoQuandoJaExisteCandidaturaParaMesmaVaga() {
        when(vagaRepository.findById(10L)).thenReturn(Optional.of(vaga));
        when(candidaturaRepository.existsByVagaIdAndCandidatoId(10L, 2L)).thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> candidaturaService.candidatar(10L, candidato)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("ja se candidatou"));
        verify(candidaturaRepository, never()).save(any());
    }

    @Test
    void candidatar_deveNotificarResponsavelEcandidatoAoTerSucesso() {
        when(vagaRepository.findById(10L)).thenReturn(Optional.of(vaga));
        when(candidaturaRepository.existsByVagaIdAndCandidatoId(10L, 2L)).thenReturn(false);
        when(candidaturaRepository.save(any(Candidatura.class))).thenAnswer(inv -> inv.getArgument(0));

        CandidaturaResponse response = candidaturaService.candidatar(10L, candidato);

        assertEquals("Dev Backend", response.getVagaTitulo());
        assertEquals("Joao Candidato", response.getCandidatoNome());

        verify(notificacaoService).notificar(eq(admin), anyString());
        verify(notificacaoService).notificar(eq(candidato), anyString());
    }

    @Test
    void listarPorVaga_deveLancarExcecaoQuandoVagaNaoExiste() {
        when(vagaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> candidaturaService.listarPorVaga(99L));
    }
}