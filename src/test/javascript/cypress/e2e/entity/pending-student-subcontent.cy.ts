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

describe('PendingStudentSubcontent e2e test', () => {
  const pendingStudentSubcontentPageUrl = '/pending-student-subcontent';
  const pendingStudentSubcontentPageUrlPattern = new RegExp('/pending-student-subcontent(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pendingStudentSubcontentSample = { currentStatus: 'shady' };

  let pendingStudentSubcontent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pending-student-subcontents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pending-student-subcontents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pending-student-subcontents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pendingStudentSubcontent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pending-student-subcontents/${pendingStudentSubcontent.id}`,
      }).then(() => {
        pendingStudentSubcontent = undefined;
      });
    }
  });

  it('PendingStudentSubcontents menu should load PendingStudentSubcontents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pending-student-subcontent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PendingStudentSubcontent').should('exist');
    cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
  });

  describe('PendingStudentSubcontent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pendingStudentSubcontentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PendingStudentSubcontent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pending-student-subcontent/new$'));
        cy.getEntityCreateUpdateHeading('PendingStudentSubcontent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pending-student-subcontents',
          body: pendingStudentSubcontentSample,
        }).then(({ body }) => {
          pendingStudentSubcontent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pending-student-subcontents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pending-student-subcontents?page=0&size=20>; rel="last",<http://localhost/api/pending-student-subcontents?page=0&size=20>; rel="first"',
              },
              body: [pendingStudentSubcontent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pendingStudentSubcontentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PendingStudentSubcontent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pendingStudentSubcontent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
      });

      it('edit button click should load edit PendingStudentSubcontent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PendingStudentSubcontent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
      });

      it('edit button click should load edit PendingStudentSubcontent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PendingStudentSubcontent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
      });

      it('last delete button click should delete instance of PendingStudentSubcontent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pendingStudentSubcontent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pendingStudentSubcontentPageUrlPattern);

        pendingStudentSubcontent = undefined;
      });
    });
  });

  describe('new PendingStudentSubcontent page', () => {
    beforeEach(() => {
      cy.visit(`${pendingStudentSubcontentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PendingStudentSubcontent');
    });

    it('should create an instance of PendingStudentSubcontent', () => {
      cy.get(`[data-cy="studentId"]`).type('defiantly compete tenderly');
      cy.get(`[data-cy="studentId"]`).should('have.value', 'defiantly compete tenderly');

      cy.get(`[data-cy="subcontentId"]`).type('how randomize');
      cy.get(`[data-cy="subcontentId"]`).should('have.value', 'how randomize');

      cy.get(`[data-cy="currentStatus"]`).type('diligently but');
      cy.get(`[data-cy="currentStatus"]`).should('have.value', 'diligently but');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pendingStudentSubcontent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pendingStudentSubcontentPageUrlPattern);
    });
  });
});
