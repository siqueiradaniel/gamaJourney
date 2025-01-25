import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILearningPath, NewLearningPath } from '../learning-path.model';

export type PartialUpdateLearningPath = Partial<ILearningPath> & Pick<ILearningPath, 'id'>;

export type EntityResponseType = HttpResponse<ILearningPath>;
export type EntityArrayResponseType = HttpResponse<ILearningPath[]>;

@Injectable({ providedIn: 'root' })
export class LearningPathService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/learning-paths');

  create(learningPath: NewLearningPath): Observable<EntityResponseType> {
    return this.http.post<ILearningPath>(this.resourceUrl, learningPath, { observe: 'response' });
  }

  update(learningPath: ILearningPath): Observable<EntityResponseType> {
    return this.http.put<ILearningPath>(`${this.resourceUrl}/${this.getLearningPathIdentifier(learningPath)}`, learningPath, {
      observe: 'response',
    });
  }

  partialUpdate(learningPath: PartialUpdateLearningPath): Observable<EntityResponseType> {
    return this.http.patch<ILearningPath>(`${this.resourceUrl}/${this.getLearningPathIdentifier(learningPath)}`, learningPath, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILearningPath>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILearningPath[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLearningPathIdentifier(learningPath: Pick<ILearningPath, 'id'>): number {
    return learningPath.id;
  }

  compareLearningPath(o1: Pick<ILearningPath, 'id'> | null, o2: Pick<ILearningPath, 'id'> | null): boolean {
    return o1 && o2 ? this.getLearningPathIdentifier(o1) === this.getLearningPathIdentifier(o2) : o1 === o2;
  }

  addLearningPathToCollectionIfMissing<Type extends Pick<ILearningPath, 'id'>>(
    learningPathCollection: Type[],
    ...learningPathsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const learningPaths: Type[] = learningPathsToCheck.filter(isPresent);
    if (learningPaths.length > 0) {
      const learningPathCollectionIdentifiers = learningPathCollection.map(learningPathItem =>
        this.getLearningPathIdentifier(learningPathItem),
      );
      const learningPathsToAdd = learningPaths.filter(learningPathItem => {
        const learningPathIdentifier = this.getLearningPathIdentifier(learningPathItem);
        if (learningPathCollectionIdentifiers.includes(learningPathIdentifier)) {
          return false;
        }
        learningPathCollectionIdentifiers.push(learningPathIdentifier);
        return true;
      });
      return [...learningPathsToAdd, ...learningPathCollection];
    }
    return learningPathCollection;
  }
}
