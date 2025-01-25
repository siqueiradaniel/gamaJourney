import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudent, NewStudent } from '../student.model';

export type PartialUpdateStudent = Partial<IStudent> & Pick<IStudent, 'id'>;

export type EntityResponseType = HttpResponse<IStudent>;
export type EntityArrayResponseType = HttpResponse<IStudent[]>;

@Injectable({ providedIn: 'root' })
export class StudentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/students');

  create(student: NewStudent): Observable<EntityResponseType> {
    return this.http.post<IStudent>(this.resourceUrl, student, { observe: 'response' });
  }

  update(student: IStudent): Observable<EntityResponseType> {
    return this.http.put<IStudent>(`${this.resourceUrl}/${this.getStudentIdentifier(student)}`, student, { observe: 'response' });
  }

  partialUpdate(student: PartialUpdateStudent): Observable<EntityResponseType> {
    return this.http.patch<IStudent>(`${this.resourceUrl}/${this.getStudentIdentifier(student)}`, student, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IStudent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStudentIdentifier(student: Pick<IStudent, 'id'>): string {
    return student.id;
  }

  compareStudent(o1: Pick<IStudent, 'id'> | null, o2: Pick<IStudent, 'id'> | null): boolean {
    return o1 && o2 ? this.getStudentIdentifier(o1) === this.getStudentIdentifier(o2) : o1 === o2;
  }

  addStudentToCollectionIfMissing<Type extends Pick<IStudent, 'id'>>(
    studentCollection: Type[],
    ...studentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const students: Type[] = studentsToCheck.filter(isPresent);
    if (students.length > 0) {
      const studentCollectionIdentifiers = studentCollection.map(studentItem => this.getStudentIdentifier(studentItem));
      const studentsToAdd = students.filter(studentItem => {
        const studentIdentifier = this.getStudentIdentifier(studentItem);
        if (studentCollectionIdentifiers.includes(studentIdentifier)) {
          return false;
        }
        studentCollectionIdentifiers.push(studentIdentifier);
        return true;
      });
      return [...studentsToAdd, ...studentCollection];
    }
    return studentCollection;
  }
}
