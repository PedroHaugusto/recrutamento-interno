import { Component, inject, signal, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { VagaService } from '../../core/services/vaga.service';
import { Vaga } from '../../models/vaga.model';

@Component({
  selector: 'app-vaga-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './vaga-form-dialog.component.html',
  styleUrl: './vaga-form-dialog.component.scss'
})
export class VagaFormDialogComponent {

  private readonly fb = inject(FormBuilder);
  private readonly vagaService = inject(VagaService);
  private readonly dialogRef = inject(MatDialogRef<VagaFormDialogComponent>);

  readonly salvando = signal(false);
  readonly erro = signal<string | null>(null);
  readonly ehEdicao: boolean;

  readonly form = this.fb.group({
    titulo: ['', [Validators.required, Validators.maxLength(150)]],
    descricao: ['', [Validators.required]],
    requisitos: [''],
    status: ['ABERTA' as 'ABERTA' | 'FECHADA']
  });

  constructor(@Inject(MAT_DIALOG_DATA) public vagaExistente: Vaga | null) {
    this.ehEdicao = !!vagaExistente;

    if (vagaExistente) {
      this.form.patchValue({
        titulo: vagaExistente.titulo,
        descricao: vagaExistente.descricao,
        requisitos: vagaExistente.requisitos ?? '',
        status: vagaExistente.status
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.salvando.set(true);
    this.erro.set(null);

    const valores = this.form.getRawValue();
    const request = {
      titulo: valores.titulo!,
      descricao: valores.descricao!,
      requisitos: valores.requisitos || undefined,
      status: valores.status!
    };

    const operacao = this.ehEdicao
      ? this.vagaService.atualizar(this.vagaExistente!.id, request)
      : this.vagaService.criar(request);

    operacao.subscribe({
      next: () => {
        this.salvando.set(false);
        this.dialogRef.close(true);
      },
      error: () => {
        this.salvando.set(false);
        this.erro.set('Não foi possível salvar a vaga. Tente novamente.');
      }
    });
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }
}
