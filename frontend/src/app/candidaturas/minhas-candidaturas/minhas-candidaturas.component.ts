import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CandidaturaService } from '../../core/services/candidatura.service';
import { Candidatura, StatusCandidatura } from '../../models/candidatura.model';

const ROTULOS_STATUS: Record<StatusCandidatura, string> = {
  PENDENTE: 'Pendente',
  EM_ANALISE: 'Em análise',
  APROVADO: 'Aprovado',
  REJEITADO: 'Rejeitado'
};

@Component({
  selector: 'app-minhas-candidaturas',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatChipsModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './minhas-candidaturas.component.html',
  styleUrl: './minhas-candidaturas.component.scss'
})
export class MinhasCandidaturasComponent implements OnInit {

  private readonly candidaturaService = inject(CandidaturaService);

  readonly candidaturas = signal<Candidatura[]>([]);
  readonly carregando = signal(true);

  ngOnInit(): void {
    this.candidaturaService.listarMinhas().subscribe({
      next: (candidaturas) => {
        const ordenadas = [...candidaturas].sort(
          (a, b) => new Date(b.dataCandidatura).getTime() - new Date(a.dataCandidatura).getTime()
        );
        this.candidaturas.set(ordenadas);
        this.carregando.set(false);
      },
      error: () => this.carregando.set(false)
    });
  }

  rotuloStatus(status: StatusCandidatura): string {
    return ROTULOS_STATUS[status];
  }

  formatarData(iso: string): string {
    return new Date(iso).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }
}
