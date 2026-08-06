import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, UsuarioLogado } from '../../models/auth.model';

const TOKEN_KEY = 'recrutamento_token';
const USUARIO_KEY = 'recrutamento_usuario';

@Injectable({ providedIn: 'root' })
export class AuthService {

  // Signal com o usuario atualmente logado (ou null se deslogado).
  // Inicializado a partir do que estiver salvo no localStorage,
  // para sobreviver a um F5 na pagina.
  private readonly usuarioSignal = signal<UsuarioLogado | null>(this.carregarUsuarioSalvo());

  readonly usuario = this.usuarioSignal.asReadonly();
  readonly estaLogado = computed(() => this.usuarioSignal() !== null);
  readonly ehAdmin = computed(() => this.usuarioSignal()?.role === 'ADMIN');

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}/auth/login`, request)
      .pipe(tap((response) => this.salvarSessao(response)));
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}/auth/register`, request)
      .pipe(tap((response) => this.salvarSessao(response)));
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USUARIO_KEY);
    this.usuarioSignal.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private salvarSessao(response: AuthResponse): void {
    const usuario: UsuarioLogado = {
      nome: response.nome,
      email: response.email,
      role: response.role
    };

    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USUARIO_KEY, JSON.stringify(usuario));
    this.usuarioSignal.set(usuario);
  }

  private carregarUsuarioSalvo(): UsuarioLogado | null {
    const raw = localStorage.getItem(USUARIO_KEY);
    return raw ? (JSON.parse(raw) as UsuarioLogado) : null;
  }
}
