package com.pacto.recrutamento.controller;

import com.pacto.recrutamento.dto.CandidaturaResponse;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.service.CandidaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Candidaturas", description = "Candidatura a vagas e consulta de status")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    @PostMapping("/vagas/{vagaId}/candidaturas")
    @Operation(summary = "Candidata-se a uma vaga (somente CANDIDATO)")
    public ResponseEntity<CandidaturaResponse> candidatar(
            @PathVariable Long vagaId,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        CandidaturaResponse response = candidaturaService.candidatar(vagaId, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/candidaturas/minhas")
    @Operation(summary = "Lista as candidaturas do usuario logado")
    public ResponseEntity<List<CandidaturaResponse>> listarMinhas(
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        return ResponseEntity.ok(candidaturaService.listarMinhas(usuarioLogado.getId()));
    }

    @GetMapping("/vagas/{vagaId}/candidaturas")
    @Operation(summary = "Lista as candidaturas de uma vaga (somente ADMIN)")
    public ResponseEntity<List<CandidaturaResponse>> listarPorVaga(@PathVariable Long vagaId) {
        return ResponseEntity.ok(candidaturaService.listarPorVaga(vagaId));
    }
}