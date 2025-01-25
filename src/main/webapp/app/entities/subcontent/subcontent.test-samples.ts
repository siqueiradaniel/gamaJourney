import { ISubcontent, NewSubcontent } from './subcontent.model';

export const sampleWithRequiredData: ISubcontent = {
  id: 'ca7e3c68-c9f2-4d37-8552-d541da406812',
};

export const sampleWithPartialData: ISubcontent = {
  id: 'ff581e86-d29b-469e-9f42-8a72ac547469',
  description: 'and inventory boo',
};

export const sampleWithFullData: ISubcontent = {
  id: '97bbaf02-89cc-4717-bfbf-bd283f533785',
  name: 'blah',
  description: 'cheese form',
};

export const sampleWithNewData: NewSubcontent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
