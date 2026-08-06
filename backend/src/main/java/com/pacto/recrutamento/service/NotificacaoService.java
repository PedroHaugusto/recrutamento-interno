package com.pacto.recrutamento.service;

import com.pacto.recrutamento.model.Notificacao;
import com.pacto.recrutamento.model.Usuario;
import com.pacto.recrutamento.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public void notificar(Usuario usuario, String mensagem) {
        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .mensagem(mensagem)
                .lida(false)
                .build();

        notificacaoRepository.save(notificacao);
    }
}