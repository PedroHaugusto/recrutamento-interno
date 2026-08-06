package com.pacto.recrutamento.service;

import com.pacto.recrutamento.dto.VagaRequest;
import com.pacto.recrutamento.dto.VagaResponse;
import com.pacto.recrutamento.exception.ResourceNotFoundException;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.model.Vaga;
import com.pacto.recrutamento.model.enums.StatusVaga;
import com.pacto.recrutamento.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    public List<VagaResponse> listarTodas() {
        return vagaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VagaResponse buscarPorId(Long id) {
        Vaga vaga = buscarEntidadePorId(id);
        return toResponse(vaga);
    }

    public VagaResponse criar(VagaRequest request, Usuario responsavel) {
        Vaga vaga = Vaga.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .requisitos(request.getRequisitos())
                .status(StatusVaga.ABERTA)
                .responsavel(responsavel)
                .build();

        vagaRepository.save(vaga);
        return toResponse(vaga);
    }

    public VagaResponse atualizar(Long id, VagaRequest request) {
        Vaga vaga = buscarEntidadePorId(id);

        vaga.setTitulo(request.getTitulo());
        vaga.setDescricao(request.getDescricao());
        vaga.setRequisitos(request.getRequisitos());

        if (request.getStatus() != null) {
            vaga.setStatus(request.getStatus());
        }

        vagaRepository.save(vaga);
        return toResponse(vaga);
    }

    public void excluir(Long id) {
        Vaga vaga = buscarEntidadePorId(id);
        vagaRepository.delete(vaga);
    }

    private Vaga buscarEntidadePorId(Long id) {
        return vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga nao encontrada: id " + id));
    }

    private VagaResponse toResponse(Vaga vaga) {
        return VagaResponse.builder()
                .id(vaga.getId())
                .titulo(vaga.getTitulo())
                .descricao(vaga.getDescricao())
                .requisitos(vaga.getRequisitos())
                .status(vaga.getStatus())
                .responsavelNome(vaga.getResponsavel().getNome())
                .dataCriacao(vaga.getDataCriacao())
                .build();
    }
}