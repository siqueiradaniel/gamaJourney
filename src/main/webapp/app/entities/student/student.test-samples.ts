import { IStudent, NewStudent } from './student.model';

export const sampleWithRequiredData: IStudent = {
  id: '7ffedc6f-8ad4-422a-a63e-daab75870a5a',
};

export const sampleWithPartialData: IStudent = {
  id: 'e65a41a3-5468-480d-873a-198e922e9357',
  name: 'finally',
};

export const sampleWithFullData: IStudent = {
  id: '3bc6e67c-9554-40f3-9a99-64bd90a94095',
  name: 'including abscond',
};

export const sampleWithNewData: NewStudent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
