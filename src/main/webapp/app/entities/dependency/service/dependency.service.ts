import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDependency, NewDependency } from '../dependency.model';

export type PartialUpdateDependency = Partial<IDependency> & Pick<IDependency, 'id'>;

export type EntityResponseType = HttpResponse<IDependency>;
export type EntityArrayResponseType = HttpResponse<IDependency[]>;

@Injectable({ providedIn: 'root' })
export class DependencyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dependencies');

  create(dependency: NewDependency): Observable<EntityResponseType> {
    return this.http.post<IDependency>(this.resourceUrl, dependency, { observe: 'response' });
  }

  update(dependency: IDependency): Observable<EntityResponseType> {
    return this.http.put<IDependency>(`${this.resourceUrl}/${this.getDependencyIdentifier(dependency)}`, dependency, {
      observe: 'response',
    });
  }

  partialUpdate(dependency: PartialUpdateDependency): Observable<EntityResponseType> {
    return this.http.patch<IDependency>(`${this.resourceUrl}/${this.getDependencyIdentifier(dependency)}`, dependency, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDependency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDependency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDependencyIdentifier(dependency: Pick<IDependency, 'id'>): number {
    return dependency.id;
  }

  compareDependency(o1: Pick<IDependency, 'id'> | null, o2: Pick<IDependency, 'id'> | null): boolean {
    return o1 && o2 ? this.getDependencyIdentifier(o1) === this.getDependencyIdentifier(o2) : o1 === o2;
  }

  addDependencyToCollectionIfMissing<Type extends Pick<IDependency, 'id'>>(
    dependencyCollection: Type[],
    ...dependenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dependencies: Type[] = dependenciesToCheck.filter(isPresent);
    if (dependencies.length > 0) {
      const dependencyCollectionIdentifiers = dependencyCollection.map(dependencyItem => this.getDependencyIdentifier(dependencyItem));
      const dependenciesToAdd = dependencies.filter(dependencyItem => {
        const dependencyIdentifier = this.getDependencyIdentifier(dependencyItem);
        if (dependencyCollectionIdentifiers.includes(dependencyIdentifier)) {
          return false;
        }
        dependencyCollectionIdentifiers.push(dependencyIdentifier);
        return true;
      });
      return [...dependenciesToAdd, ...dependencyCollection];
    }
    return dependencyCollection;
  }
}
