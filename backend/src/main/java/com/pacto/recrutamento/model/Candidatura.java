package com.pacto.recrutamento.model;

import com.pacto.recrutamento.model.enums.StatusCandidatura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidatura",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_candidatura_vaga_candidato",
                columnNames = {"vaga_id", "candidato_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Usuario candidato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status_candidatura")
    @Builder.Default
    private StatusCandidatura status = StatusCandidatura.PENDENTE;

    @CreationTimestamp
    @Column(name = "data_candidatura", nullable = false, updatable = false)
    private LocalDateTime dataCandidatura;

    @Column(name = "tempo_experiencia_anos")
    private Integer tempoExperienciaAnos;
}