import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Dependency e2e test', () => {
  const dependencyPageUrl = '/dependency';
  const dependencyPageUrlPattern = new RegExp('/dependency(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dependencySample = {};

  let dependency;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/dependencies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dependencies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dependencies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dependency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dependencies/${dependency.id}`,
      }).then(() => {
        dependency = undefined;
      });
    }
  });

  it('Dependencies menu should load Dependencies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dependency');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dependency').should('exist');
    cy.url().should('match', dependencyPageUrlPattern);
  });

  describe('Dependency page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dependencyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dependency page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dependency/new$'));
        cy.getEntityCreateUpdateHeading('Dependency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dependencyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dependencies',
          body: dependencySample,
        }).then(({ body }) => {
          dependency = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dependencies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dependency],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dependencyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dependency page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dependency');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dependencyPageUrlPattern);
      });

      it('edit button click should load edit Dependency page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dependency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dependencyPageUrlPattern);
      });

      it('edit button click should load edit Dependency page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dependency');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dependencyPageUrlPattern);
      });

      it('last delete button click should delete instance of Dependency', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dependency').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dependencyPageUrlPattern);

        dependency = undefined;
      });
    });
  });

  describe('new Dependency page', () => {
    beforeEach(() => {
      cy.visit(`${dependencyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dependency');
    });

    it('should create an instance of Dependency', () => {
      cy.get(`[data-cy="firstSubcontentId"]`).type('yak');
      cy.get(`[data-cy="firstSubcontentId"]`).should('have.value', 'yak');

      cy.get(`[data-cy="secondSubcontentId"]`).type('fuel produce');
      cy.get(`[data-cy="secondSubcontentId"]`).should('have.value', 'fuel produce');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        dependency = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', dependencyPageUrlPattern);
    });
  });
});
