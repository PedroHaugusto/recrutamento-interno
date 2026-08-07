import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Vaga, VagaRequest } from '../../models/vaga.model';

@Injectable({ providedIn: 'root' })
export class VagaService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/vagas`;

  listar(): Observable<Vaga[]> {
    return this.http.get<Vaga[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Vaga> {
    return this.http.get<Vaga>(`${this.baseUrl}/${id}`);
  }

  criar(request: VagaRequest): Observable<Vaga> {
    return this.http.post<Vaga>(this.baseUrl, request);
  }

  atualizar(id: number, request: VagaRequest): Observable<Vaga> {
    return this.http.put<Vaga>(`${this.baseUrl}/${id}`, request);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
