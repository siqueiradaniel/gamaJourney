package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PendingStudentSubcontentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PendingStudentSubcontent;
import com.mycompany.myapp.repository.PendingStudentSubcontentRepository;
import com.mycompany.myapp.service.dto.PendingStudentSubcontentDTO;
import com.mycompany.myapp.service.mapper.PendingStudentSubcontentMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PendingStudentSubcontentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PendingStudentSubcontentResourceIT {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SUBCONTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUBCONTENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pending-student-subcontents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PendingStudentSubcontentRepository pendingStudentSubcontentRepository;

    @Autowired
    private PendingStudentSubcontentMapper pendingStudentSubcontentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPendingStudentSubcontentMockMvc;

    private PendingStudentSubcontent pendingStudentSubcontent;

    private PendingStudentSubcontent insertedPendingStudentSubcontent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PendingStudentSubcontent createEntity() {
        return new PendingStudentSubcontent()
            .studentId(DEFAULT_STUDENT_ID)
            .subcontentId(DEFAULT_SUBCONTENT_ID)
            .currentStatus(DEFAULT_CURRENT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PendingStudentSubcontent createUpdatedEntity() {
        return new PendingStudentSubcontent()
            .studentId(UPDATED_STUDENT_ID)
            .subcontentId(UPDATED_SUBCONTENT_ID)
            .currentStatus(UPDATED_CURRENT_STATUS);
    }

    @BeforeEach
    public void initTest() {
        pendingStudentSubcontent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPendingStudentSubcontent != null) {
            pendingStudentSubcontentRepository.delete(insertedPendingStudentSubcontent);
            insertedPendingStudentSubcontent = null;
        }
    }

    @Test
    @Transactional
    void createPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);
        var returnedPendingStudentSubcontentDTO = om.readValue(
            restPendingStudentSubcontentMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PendingStudentSubcontentDTO.class
        );

        // Validate the PendingStudentSubcontent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPendingStudentSubcontent = pendingStudentSubcontentMapper.toEntity(returnedPendingStudentSubcontentDTO);
        assertPendingStudentSubcontentUpdatableFieldsEquals(
            returnedPendingStudentSubcontent,
            getPersistedPendingStudentSubcontent(returnedPendingStudentSubcontent)
        );

        insertedPendingStudentSubcontent = returnedPendingStudentSubcontent;
    }

    @Test
    @Transactional
    void createPendingStudentSubcontentWithExistingId() throws Exception {
        // Create the PendingStudentSubcontent with an existing ID
        pendingStudentSubcontent.setId(1L);
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPendingStudentSubcontentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCurrentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pendingStudentSubcontent.setCurrentStatus(null);

        // Create the PendingStudentSubcontent, which fails.
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        restPendingStudentSubcontentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPendingStudentSubcontents() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        // Get all the pendingStudentSubcontentList
        restPendingStudentSubcontentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pendingStudentSubcontent.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].subcontentId").value(hasItem(DEFAULT_SUBCONTENT_ID)))
            .andExpect(jsonPath("$.[*].currentStatus").value(hasItem(DEFAULT_CURRENT_STATUS)));
    }

    @Test
    @Transactional
    void getPendingStudentSubcontent() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        // Get the pendingStudentSubcontent
        restPendingStudentSubcontentMockMvc
            .perform(get(ENTITY_API_URL_ID, pendingStudentSubcontent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pendingStudentSubcontent.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.subcontentId").value(DEFAULT_SUBCONTENT_ID))
            .andExpect(jsonPath("$.currentStatus").value(DEFAULT_CURRENT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingPendingStudentSubcontent() throws Exception {
        // Get the pendingStudentSubcontent
        restPendingStudentSubcontentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPendingStudentSubcontent() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pendingStudentSubcontent
        PendingStudentSubcontent updatedPendingStudentSubcontent = pendingStudentSubcontentRepository
            .findById(pendingStudentSubcontent.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPendingStudentSubcontent are not directly saved in db
        em.detach(updatedPendingStudentSubcontent);
        updatedPendingStudentSubcontent
            .studentId(UPDATED_STUDENT_ID)
            .subcontentId(UPDATED_SUBCONTENT_ID)
            .currentStatus(UPDATED_CURRENT_STATUS);
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(updatedPendingStudentSubcontent);

        restPendingStudentSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pendingStudentSubcontentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPendingStudentSubcontentToMatchAllProperties(updatedPendingStudentSubcontent);
    }

    @Test
    @Transactional
    void putNonExistingPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pendingStudentSubcontentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pendingStudentSubcontentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePendingStudentSubcontentWithPatch() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pendingStudentSubcontent using partial update
        PendingStudentSubcontent partialUpdatedPendingStudentSubcontent = new PendingStudentSubcontent();
        partialUpdatedPendingStudentSubcontent.setId(pendingStudentSubcontent.getId());

        partialUpdatedPendingStudentSubcontent.studentId(UPDATED_STUDENT_ID).subcontentId(UPDATED_SUBCONTENT_ID);

        restPendingStudentSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPendingStudentSubcontent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPendingStudentSubcontent))
            )
            .andExpect(status().isOk());

        // Validate the PendingStudentSubcontent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPendingStudentSubcontentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPendingStudentSubcontent, pendingStudentSubcontent),
            getPersistedPendingStudentSubcontent(pendingStudentSubcontent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePendingStudentSubcontentWithPatch() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pendingStudentSubcontent using partial update
        PendingStudentSubcontent partialUpdatedPendingStudentSubcontent = new PendingStudentSubcontent();
        partialUpdatedPendingStudentSubcontent.setId(pendingStudentSubcontent.getId());

        partialUpdatedPendingStudentSubcontent
            .studentId(UPDATED_STUDENT_ID)
            .subcontentId(UPDATED_SUBCONTENT_ID)
            .currentStatus(UPDATED_CURRENT_STATUS);

        restPendingStudentSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPendingStudentSubcontent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPendingStudentSubcontent))
            )
            .andExpect(status().isOk());

        // Validate the PendingStudentSubcontent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPendingStudentSubcontentUpdatableFieldsEquals(
            partialUpdatedPendingStudentSubcontent,
            getPersistedPendingStudentSubcontent(partialUpdatedPendingStudentSubcontent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pendingStudentSubcontentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPendingStudentSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pendingStudentSubcontent.setId(longCount.incrementAndGet());

        // Create the PendingStudentSubcontent
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO = pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendingStudentSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pendingStudentSubcontentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PendingStudentSubcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePendingStudentSubcontent() throws Exception {
        // Initialize the database
        insertedPendingStudentSubcontent = pendingStudentSubcontentRepository.saveAndFlush(pendingStudentSubcontent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pendingStudentSubcontent
        restPendingStudentSubcontentMockMvc
            .perform(delete(ENTITY_API_URL_ID, pendingStudentSubcontent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pendingStudentSubcontentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PendingStudentSubcontent getPersistedPendingStudentSubcontent(PendingStudentSubcontent pendingStudentSubcontent) {
        return pendingStudentSubcontentRepository.findById(pendingStudentSubcontent.getId()).orElseThrow();
    }

    protected void assertPersistedPendingStudentSubcontentToMatchAllProperties(PendingStudentSubcontent expectedPendingStudentSubcontent) {
        assertPendingStudentSubcontentAllPropertiesEquals(
            expectedPendingStudentSubcontent,
            getPersistedPendingStudentSubcontent(expectedPendingStudentSubcontent)
        );
    }

    protected void assertPersistedPendingStudentSubcontentToMatchUpdatableProperties(
        PendingStudentSubcontent expectedPendingStudentSubcontent
    ) {
        assertPendingStudentSubcontentAllUpdatablePropertiesEquals(
            expectedPendingStudentSubcontent,
            getPersistedPendingStudentSubcontent(expectedPendingStudentSubcontent)
        );
    }
}
