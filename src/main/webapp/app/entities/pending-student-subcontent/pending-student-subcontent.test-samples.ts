import { IPendingStudentSubcontent, NewPendingStudentSubcontent } from './pending-student-subcontent.model';

export const sampleWithRequiredData: IPendingStudentSubcontent = {
  id: 18432,
  currentStatus: 'excepting ack',
};

export const sampleWithPartialData: IPendingStudentSubcontent = {
  id: 15221,
  studentId: 'valentine',
  currentStatus: 'kindly oof shrilly',
};

export const sampleWithFullData: IPendingStudentSubcontent = {
  id: 13301,
  studentId: 'probe enroll louse',
  subcontentId: 'that wire',
  currentStatus: 'chip quadruple ack',
};

export const sampleWithNewData: NewPendingStudentSubcontent = {
  currentStatus: 'disloyal tricky eke',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
