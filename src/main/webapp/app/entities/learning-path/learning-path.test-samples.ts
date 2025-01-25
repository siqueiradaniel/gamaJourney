import { ILearningPath, NewLearningPath } from './learning-path.model';

export const sampleWithRequiredData: ILearningPath = {
  id: 4275,
};

export const sampleWithPartialData: ILearningPath = {
  id: 24110,
  subcontentId: 'reboot downright the',
};

export const sampleWithFullData: ILearningPath = {
  id: 27642,
  examId: 'wear',
  subcontentId: 'haunting strait scaffold',
  order: 19715,
};

export const sampleWithNewData: NewLearningPath = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
