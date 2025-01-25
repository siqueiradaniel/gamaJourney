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

describe('Subcontent e2e test', () => {
  const subcontentPageUrl = '/subcontent';
  const subcontentPageUrlPattern = new RegExp('/subcontent(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subcontentSample = {};

  let subcontent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subcontents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subcontents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subcontents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subcontent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subcontents/${subcontent.id}`,
      }).then(() => {
        subcontent = undefined;
      });
    }
  });

  it('Subcontents menu should load Subcontents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subcontent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Subcontent').should('exist');
    cy.url().should('match', subcontentPageUrlPattern);
  });

  describe('Subcontent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subcontentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Subcontent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subcontent/new$'));
        cy.getEntityCreateUpdateHeading('Subcontent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subcontentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subcontents',
          body: subcontentSample,
        }).then(({ body }) => {
          subcontent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subcontents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subcontents?page=0&size=20>; rel="last",<http://localhost/api/subcontents?page=0&size=20>; rel="first"',
              },
              body: [subcontent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subcontentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Subcontent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subcontent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subcontentPageUrlPattern);
      });

      it('edit button click should load edit Subcontent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subcontent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subcontentPageUrlPattern);
      });

      it('edit button click should load edit Subcontent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subcontent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subcontentPageUrlPattern);
      });

      it('last delete button click should delete instance of Subcontent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('subcontent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subcontentPageUrlPattern);

        subcontent = undefined;
      });
    });
  });

  describe('new Subcontent page', () => {
    beforeEach(() => {
      cy.visit(`${subcontentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Subcontent');
    });

    it('should create an instance of Subcontent', () => {
      cy.get(`[data-cy="name"]`).type('shrill past');
      cy.get(`[data-cy="name"]`).should('have.value', 'shrill past');

      cy.get(`[data-cy="description"]`).type('skeletal accidentally once');
      cy.get(`[data-cy="description"]`).should('have.value', 'skeletal accidentally once');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subcontent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subcontentPageUrlPattern);
    });
  });
});
