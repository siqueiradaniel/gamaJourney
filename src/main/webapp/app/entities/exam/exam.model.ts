export interface IExam {
  id: string;
  name?: string | null;
  description?: string | null;
}

export type NewExam = Omit<IExam, 'id'> & { id: null };
