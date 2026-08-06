package com.pacto.recrutamento.repository;

import com.pacto.recrutamento.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);
}