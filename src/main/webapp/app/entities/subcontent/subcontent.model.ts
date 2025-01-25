import { IContent } from 'app/entities/content/content.model';

export interface ISubcontent {
  id: string;
  name?: string | null;
  description?: string | null;
  content?: Pick<IContent, 'id'> | null;
}

export type NewSubcontent = Omit<ISubcontent, 'id'> & { id: null };
