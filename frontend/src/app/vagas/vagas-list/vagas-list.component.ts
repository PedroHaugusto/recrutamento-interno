import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { VagaService } from '../../core/services/vaga.service';
import { CandidaturaService } from '../../core/services/candidatura.service';
import { Vaga } from '../../models/vaga.model';
import { VagaFormDialogComponent } from '../vaga-form-dialog/vaga-form-dialog.component';

@Component({
  selector: 'app-vagas-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule
  ],
  templateUrl: './vagas-list.component.html',
  styleUrl: './vagas-list.component.scss'
})
export class VagasListComponent implements OnInit {

  private readonly vagaService = inject(VagaService);
  private readonly candidaturaService = inject(CandidaturaService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialog = inject(MatDialog);
  protected readonly authService = inject(AuthService);

  readonly vagas = signal<Vaga[]>([]);
  readonly carregando = signal(true);
  readonly candidatandoId = signal<number | null>(null);

  private readonly vagaIdsCandidatadas = signal<Set<number>>(new Set());

  readonly vagasOrdenadas = computed(() =>
    [...this.vagas()].sort((a, b) => (a.status === b.status ? 0 : a.status === 'ABERTA' ? -1 : 1))
  );

  ngOnInit(): void {
    this.carregarDados();
  }

  private carregarDados(): void {
    this.carregando.set(true);

    if (this.authService.ehAdmin()) {
      this.vagaService.listar().subscribe({
        next: (vagas) => {
          this.vagas.set(vagas);
          this.carregando.set(false);
        },
        error: () => this.carregando.set(false)
      });
      return;
    }

    forkJoin({
      vagas: this.vagaService.listar(),
      minhas: this.candidaturaService.listarMinhas()
    }).subscribe({
      next: ({ vagas, minhas }) => {
        this.vagas.set(vagas);
        this.vagaIdsCandidatadas.set(new Set(minhas.map((c) => c.vagaId)));
        this.carregando.set(false);
      },
      error: () => this.carregando.set(false)
    });
  }

  jaCandidatou(vagaId: number): boolean {
    return this.vagaIdsCandidatadas().has(vagaId);
  }

  candidatar(vaga: Vaga): void {
    this.candidatandoId.set(vaga.id);

    this.candidaturaService.candidatar(vaga.id).subscribe({
      next: () => {
        this.candidatandoId.set(null);
        const atual = new Set(this.vagaIdsCandidatadas());
        atual.add(vaga.id);
        this.vagaIdsCandidatadas.set(atual);
        this.snackBar.open('Candidatura enviada com sucesso!', 'Fechar', { duration: 4000 });
      },
      error: () => {
        this.candidatandoId.set(null);
        this.snackBar.open('Não foi possível enviar sua candidatura.', 'Fechar', { duration: 4000 });
      }
    });
  }

  abrirFormularioNovaVaga(): void {
    const ref = this.dialog.open(VagaFormDialogComponent, {
      width: '520px',
      data: null
    });

    ref.afterClosed().subscribe((salvou) => {
      if (salvou) {
        this.carregarDados();
      }
    });
  }

  editarVaga(vaga: Vaga): void {
    const ref = this.dialog.open(VagaFormDialogComponent, {
      width: '520px',
      data: vaga
    });

    ref.afterClosed().subscribe((salvou) => {
      if (salvou) {
        this.carregarDados();
      }
    });
  }

  excluirVaga(vaga: Vaga): void {
    if (!confirm(`Excluir a vaga "${vaga.titulo}"? Essa ação não pode ser desfeita.`)) {
      return;
    }

    this.vagaService.excluir(vaga.id).subscribe({
      next: () => {
        this.snackBar.open('Vaga excluída.', 'Fechar', { duration: 3000 });
        this.carregarDados();
      },
      error: () => {
        this.snackBar.open('Não foi possível excluir a vaga.', 'Fechar', { duration: 4000 });
      }
    });
  }
}
