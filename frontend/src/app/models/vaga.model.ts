export type StatusVaga = 'ABERTA' | 'FECHADA';

export interface Vaga {
  id: number;
  titulo: string;
  descricao: string;
  requisitos: string | null;
  status: StatusVaga;
  responsavelNome: string;
  dataCriacao: string;
}

export interface VagaRequest {
  titulo: string;
  descricao: string;
  requisitos?: string;
  status?: StatusVaga;
}
