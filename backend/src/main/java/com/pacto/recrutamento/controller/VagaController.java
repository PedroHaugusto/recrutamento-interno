package com.pacto.recrutamento.controller;

import com.pacto.recrutamento.dto.VagaRequest;
import com.pacto.recrutamento.dto.VagaResponse;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.service.VagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vagas")
@RequiredArgsConstructor
@Tag(name = "Vagas", description = "Cadastro e consulta de vagas internas")
public class VagaController {

    private final VagaService vagaService;

    @GetMapping
    @Operation(summary = "Lista todas as vagas (abertas e fechadas)")
    public ResponseEntity<List<VagaResponse>> listar() {
        return ResponseEntity.ok(vagaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma vaga por ID")
    public ResponseEntity<VagaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vagaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova vaga (somente ADMIN)")
    public ResponseEntity<VagaResponse> criar(
            @Valid @RequestBody VagaRequest request,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        VagaResponse response = vagaService.criar(request, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma vaga existente (somente ADMIN)")
    public ResponseEntity<VagaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VagaRequest request
    ) {
        return ResponseEntity.ok(vagaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma vaga (somente ADMIN)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}