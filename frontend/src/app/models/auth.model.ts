export type RoleUsuario = 'ADMIN' | 'CANDIDATO';

export interface AuthResponse {
  token: string;
  nome: string;
  email: string;
  role: RoleUsuario;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface UsuarioLogado {
  nome: string;
  email: string;
  role: RoleUsuario;
}
