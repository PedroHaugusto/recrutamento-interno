import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Candidatura } from '../../models/candidatura.model';

@Injectable({ providedIn: 'root' })
export class CandidaturaService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  candidatar(vagaId: number): Observable<Candidatura> {
    return this.http.post<Candidatura>(`${this.baseUrl}/vagas/${vagaId}/candidaturas`, {});
  }

  listarMinhas(): Observable<Candidatura[]> {
    return this.http.get<Candidatura[]>(`${this.baseUrl}/candidaturas/minhas`);
  }

  listarPorVaga(vagaId: number): Observable<Candidatura[]> {
    return this.http.get<Candidatura[]>(`${this.baseUrl}/vagas/${vagaId}/candidaturas`);
  }
}
