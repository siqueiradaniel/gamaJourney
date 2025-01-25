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

describe('LearningPath e2e test', () => {
  const learningPathPageUrl = '/learning-path';
  const learningPathPageUrlPattern = new RegExp('/learning-path(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const learningPathSample = {};

  let learningPath;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/learning-paths+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/learning-paths').as('postEntityRequest');
    cy.intercept('DELETE', '/api/learning-paths/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (learningPath) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/learning-paths/${learningPath.id}`,
      }).then(() => {
        learningPath = undefined;
      });
    }
  });

  it('LearningPaths menu should load LearningPaths page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('learning-path');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LearningPath').should('exist');
    cy.url().should('match', learningPathPageUrlPattern);
  });

  describe('LearningPath page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(learningPathPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LearningPath page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/learning-path/new$'));
        cy.getEntityCreateUpdateHeading('LearningPath');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', learningPathPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/learning-paths',
          body: learningPathSample,
        }).then(({ body }) => {
          learningPath = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/learning-paths+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [learningPath],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(learningPathPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LearningPath page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('learningPath');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', learningPathPageUrlPattern);
      });

      it('edit button click should load edit LearningPath page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LearningPath');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', learningPathPageUrlPattern);
      });

      it('edit button click should load edit LearningPath page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LearningPath');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', learningPathPageUrlPattern);
      });

      it('last delete button click should delete instance of LearningPath', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('learningPath').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', learningPathPageUrlPattern);

        learningPath = undefined;
      });
    });
  });

  describe('new LearningPath page', () => {
    beforeEach(() => {
      cy.visit(`${learningPathPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LearningPath');
    });

    it('should create an instance of LearningPath', () => {
      cy.get(`[data-cy="examId"]`).type('voluntarily');
      cy.get(`[data-cy="examId"]`).should('have.value', 'voluntarily');

      cy.get(`[data-cy="subcontentId"]`).type('yum evenly');
      cy.get(`[data-cy="subcontentId"]`).should('have.value', 'yum evenly');

      cy.get(`[data-cy="order"]`).type('13913');
      cy.get(`[data-cy="order"]`).should('have.value', '13913');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        learningPath = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', learningPathPageUrlPattern);
    });
  });
});
