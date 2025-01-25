import { IDependency, NewDependency } from './dependency.model';

export const sampleWithRequiredData: IDependency = {
  id: 9593,
};

export const sampleWithPartialData: IDependency = {
  id: 18979,
  firstSubcontentId: 'instead',
};

export const sampleWithFullData: IDependency = {
  id: 5984,
  firstSubcontentId: 'kaleidoscopic urgently',
  secondSubcontentId: 'outlaw voluntarily',
};

export const sampleWithNewData: NewDependency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
