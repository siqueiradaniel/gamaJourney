import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubcontent, NewSubcontent } from '../subcontent.model';

export type PartialUpdateSubcontent = Partial<ISubcontent> & Pick<ISubcontent, 'id'>;

export type EntityResponseType = HttpResponse<ISubcontent>;
export type EntityArrayResponseType = HttpResponse<ISubcontent[]>;

@Injectable({ providedIn: 'root' })
export class SubcontentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subcontents');

  create(subcontent: NewSubcontent): Observable<EntityResponseType> {
    return this.http.post<ISubcontent>(this.resourceUrl, subcontent, { observe: 'response' });
  }

  update(subcontent: ISubcontent): Observable<EntityResponseType> {
    return this.http.put<ISubcontent>(`${this.resourceUrl}/${this.getSubcontentIdentifier(subcontent)}`, subcontent, {
      observe: 'response',
    });
  }

  partialUpdate(subcontent: PartialUpdateSubcontent): Observable<EntityResponseType> {
    return this.http.patch<ISubcontent>(`${this.resourceUrl}/${this.getSubcontentIdentifier(subcontent)}`, subcontent, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISubcontent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubcontent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubcontentIdentifier(subcontent: Pick<ISubcontent, 'id'>): string {
    return subcontent.id;
  }

  compareSubcontent(o1: Pick<ISubcontent, 'id'> | null, o2: Pick<ISubcontent, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubcontentIdentifier(o1) === this.getSubcontentIdentifier(o2) : o1 === o2;
  }

  addSubcontentToCollectionIfMissing<Type extends Pick<ISubcontent, 'id'>>(
    subcontentCollection: Type[],
    ...subcontentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subcontents: Type[] = subcontentsToCheck.filter(isPresent);
    if (subcontents.length > 0) {
      const subcontentCollectionIdentifiers = subcontentCollection.map(subcontentItem => this.getSubcontentIdentifier(subcontentItem));
      const subcontentsToAdd = subcontents.filter(subcontentItem => {
        const subcontentIdentifier = this.getSubcontentIdentifier(subcontentItem);
        if (subcontentCollectionIdentifiers.includes(subcontentIdentifier)) {
          return false;
        }
        subcontentCollectionIdentifiers.push(subcontentIdentifier);
        return true;
      });
      return [...subcontentsToAdd, ...subcontentCollection];
    }
    return subcontentCollection;
  }
}
