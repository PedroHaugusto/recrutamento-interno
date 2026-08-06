export type StatusCandidatura = 'PENDENTE' | 'EM_ANALISE' | 'APROVADO' | 'REJEITADO';

export interface Candidatura {
  id: number;
  vagaId: number;
  vagaTitulo: string;
  candidatoNome: string;
  status: StatusCandidatura;
  dataCandidatura: string;
}
