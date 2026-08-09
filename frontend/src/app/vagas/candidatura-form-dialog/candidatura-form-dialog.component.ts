import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Vaga } from '../../models/vaga.model';

export interface CandidaturaFormResultado {
  tempoExperienciaAnos: number;
}

@Component({
  selector: 'app-candidatura-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './candidatura-form-dialog.component.html',
  styleUrl: './candidatura-form-dialog.component.scss'
})
export class CandidaturaFormDialogComponent {

  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<CandidaturaFormDialogComponent>);

  readonly form = this.fb.group({
    tempoExperienciaAnos: [
      null as number | null,
      [Validators.required, Validators.min(0), Validators.max(60)]
    ]
  });

  constructor(@Inject(MAT_DIALOG_DATA) public vaga: Vaga) {}

  confirmar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const resultado: CandidaturaFormResultado = {
      tempoExperienciaAnos: this.form.getRawValue().tempoExperienciaAnos!
    };

    this.dialogRef.close(resultado);
  }

  cancelar(): void {
    this.dialogRef.close(null);
  }
}
