export interface IContent {
  id: string;
  name?: string | null;
  description?: string | null;
}

export type NewContent = Omit<IContent, 'id'> & { id: null };
