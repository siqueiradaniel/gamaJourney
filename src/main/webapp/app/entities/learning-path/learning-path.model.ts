import { IExam } from 'app/entities/exam/exam.model';
import { ISubcontent } from 'app/entities/subcontent/subcontent.model';

export interface ILearningPath {
  id: number;
  examId?: string | null;
  subcontentId?: string | null;
  order?: number | null;
  exam?: Pick<IExam, 'id'> | null;
  subcontent?: Pick<ISubcontent, 'id'> | null;
}

export type NewLearningPath = Omit<ILearningPath, 'id'> & { id: null };
