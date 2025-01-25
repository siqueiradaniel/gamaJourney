import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPendingStudentSubcontent, NewPendingStudentSubcontent } from '../pending-student-subcontent.model';

export type PartialUpdatePendingStudentSubcontent = Partial<IPendingStudentSubcontent> & Pick<IPendingStudentSubcontent, 'id'>;

export type EntityResponseType = HttpResponse<IPendingStudentSubcontent>;
export type EntityArrayResponseType = HttpResponse<IPendingStudentSubcontent[]>;

@Injectable({ providedIn: 'root' })
export class PendingStudentSubcontentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pending-student-subcontents');

  create(pendingStudentSubcontent: NewPendingStudentSubcontent): Observable<EntityResponseType> {
    return this.http.post<IPendingStudentSubcontent>(this.resourceUrl, pendingStudentSubcontent, { observe: 'response' });
  }

  update(pendingStudentSubcontent: IPendingStudentSubcontent): Observable<EntityResponseType> {
    return this.http.put<IPendingStudentSubcontent>(
      `${this.resourceUrl}/${this.getPendingStudentSubcontentIdentifier(pendingStudentSubcontent)}`,
      pendingStudentSubcontent,
      { observe: 'response' },
    );
  }

  partialUpdate(pendingStudentSubcontent: PartialUpdatePendingStudentSubcontent): Observable<EntityResponseType> {
    return this.http.patch<IPendingStudentSubcontent>(
      `${this.resourceUrl}/${this.getPendingStudentSubcontentIdentifier(pendingStudentSubcontent)}`,
      pendingStudentSubcontent,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPendingStudentSubcontent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPendingStudentSubcontent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPendingStudentSubcontentIdentifier(pendingStudentSubcontent: Pick<IPendingStudentSubcontent, 'id'>): number {
    return pendingStudentSubcontent.id;
  }

  comparePendingStudentSubcontent(
    o1: Pick<IPendingStudentSubcontent, 'id'> | null,
    o2: Pick<IPendingStudentSubcontent, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getPendingStudentSubcontentIdentifier(o1) === this.getPendingStudentSubcontentIdentifier(o2) : o1 === o2;
  }

  addPendingStudentSubcontentToCollectionIfMissing<Type extends Pick<IPendingStudentSubcontent, 'id'>>(
    pendingStudentSubcontentCollection: Type[],
    ...pendingStudentSubcontentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pendingStudentSubcontents: Type[] = pendingStudentSubcontentsToCheck.filter(isPresent);
    if (pendingStudentSubcontents.length > 0) {
      const pendingStudentSubcontentCollectionIdentifiers = pendingStudentSubcontentCollection.map(pendingStudentSubcontentItem =>
        this.getPendingStudentSubcontentIdentifier(pendingStudentSubcontentItem),
      );
      const pendingStudentSubcontentsToAdd = pendingStudentSubcontents.filter(pendingStudentSubcontentItem => {
        const pendingStudentSubcontentIdentifier = this.getPendingStudentSubcontentIdentifier(pendingStudentSubcontentItem);
        if (pendingStudentSubcontentCollectionIdentifiers.includes(pendingStudentSubcontentIdentifier)) {
          return false;
        }
        pendingStudentSubcontentCollectionIdentifiers.push(pendingStudentSubcontentIdentifier);
        return true;
      });
      return [...pendingStudentSubcontentsToAdd, ...pendingStudentSubcontentCollection];
    }
    return pendingStudentSubcontentCollection;
  }
}
