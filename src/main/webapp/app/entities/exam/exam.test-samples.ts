import { IExam, NewExam } from './exam.model';

export const sampleWithRequiredData: IExam = {
  id: '9f7a38c3-ada1-4d14-aef7-fd2eecc0fa00',
};

export const sampleWithPartialData: IExam = {
  id: '94ca6440-15d8-4a64-b392-4111f1058625',
  name: 'extent',
  description: 'ack',
};

export const sampleWithFullData: IExam = {
  id: 'aaad6fdc-267a-44cd-be49-77de70a04a79',
  name: 'stint until',
  description: 'even',
};

export const sampleWithNewData: NewExam = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
