package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.CandidaturaResponse;
import com.pacto.recrutamento.exception.BusinessException;
import com.pacto.recrutamento.exception.ResourceNotFoundException;
import com.pacto.recrutamento.model.Candidatura;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.Vaga;
import com.pacto.recrutamento.repository.CandidaturaRepository;
import com.pacto.recrutamento.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final VagaRepository vagaRepository;
    private final NotificacaoService notificacaoService;

    public CandidaturaResponse candidatar(Long vagaId, Usuario candidato) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga nao encontrada: id " + vagaId));

        if (candidaturaRepository.existsByVagaIdAndCandidatoId(vagaId, candidato.getId())) {
            throw new BusinessException("Voce ja se candidatou a esta vaga");
        }

        Candidatura candidatura = Candidatura.builder()
                .vaga(vaga)
                .candidato(candidato)
                .build();

        candidaturaRepository.save(candidatura);

        // Notifica o responsavel pela vaga e o proprio candidato
        notificacaoService.notificar(
                vaga.getResponsavel(),
                "Nova candidatura recebida para a vaga \"" + vaga.getTitulo() + "\" de " + candidato.getNome()
        );
        notificacaoService.notificar(
                candidato,
                "Sua candidatura para a vaga \"" + vaga.getTitulo() + "\" foi registrada com sucesso"
        );

        return toResponse(candidatura);
    }

    public List<CandidaturaResponse> listarMinhas(Long candidatoId) {
        return candidaturaRepository.findByCandidatoId(candidatoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CandidaturaResponse> listarPorVaga(Long vagaId) {
        if (!vagaRepository.existsById(vagaId)) {
            throw new ResourceNotFoundException("Vaga nao encontrada: id " + vagaId);
        }

        return candidaturaRepository.findByVagaId(vagaId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CandidaturaResponse toResponse(Candidatura candidatura) {
        return CandidaturaResponse.builder()
                .id(candidatura.getId())
                .vagaId(candidatura.getVaga().getId())
                .vagaTitulo(candidatura.getVaga().getTitulo())
                .candidatoNome(candidatura.getCandidato().getNome())
                .status(candidatura.getStatus())
                .dataCandidatura(candidatura.getDataCandidatura())
                .build();
    }
}