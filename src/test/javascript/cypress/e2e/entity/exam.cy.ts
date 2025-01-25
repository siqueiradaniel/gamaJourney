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

describe('Exam e2e test', () => {
  const examPageUrl = '/exam';
  const examPageUrlPattern = new RegExp('/exam(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const examSample = {};

  let exam;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/exams+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/exams').as('postEntityRequest');
    cy.intercept('DELETE', '/api/exams/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (exam) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/exams/${exam.id}`,
      }).then(() => {
        exam = undefined;
      });
    }
  });

  it('Exams menu should load Exams page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('exam');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Exam').should('exist');
    cy.url().should('match', examPageUrlPattern);
  });

  describe('Exam page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(examPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Exam page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/exam/new$'));
        cy.getEntityCreateUpdateHeading('Exam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/exams',
          body: examSample,
        }).then(({ body }) => {
          exam = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/exams+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/exams?page=0&size=20>; rel="last",<http://localhost/api/exams?page=0&size=20>; rel="first"',
              },
              body: [exam],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(examPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Exam page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('exam');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });

      it('edit button click should load edit Exam page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Exam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });

      it('edit button click should load edit Exam page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Exam');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });

      it('last delete button click should delete instance of Exam', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('exam').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);

        exam = undefined;
      });
    });
  });

  describe('new Exam page', () => {
    beforeEach(() => {
      cy.visit(`${examPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Exam');
    });

    it('should create an instance of Exam', () => {
      cy.get(`[data-cy="name"]`).type('uh-huh');
      cy.get(`[data-cy="name"]`).should('have.value', 'uh-huh');

      cy.get(`[data-cy="description"]`).type('besides');
      cy.get(`[data-cy="description"]`).should('have.value', 'besides');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        exam = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', examPageUrlPattern);
    });
  });
});
