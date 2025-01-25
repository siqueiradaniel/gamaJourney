import { IExam } from 'app/entities/exam/exam.model';

export interface IStudent {
  id: string;
  name?: string | null;
  exam?: Pick<IExam, 'id'> | null;
}

export type NewStudent = Omit<IStudent, 'id'> & { id: null };
