package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SubcontentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.repository.SubcontentRepository;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import com.mycompany.myapp.service.mapper.SubcontentMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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
 * Integration tests for the {@link SubcontentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubcontentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subcontents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubcontentRepository subcontentRepository;

    @Autowired
    private SubcontentMapper subcontentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubcontentMockMvc;

    private Subcontent subcontent;

    private Subcontent insertedSubcontent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subcontent createEntity() {
        return new Subcontent().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subcontent createUpdatedEntity() {
        return new Subcontent().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        subcontent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubcontent != null) {
            subcontentRepository.delete(insertedSubcontent);
            insertedSubcontent = null;
        }
    }

    @Test
    @Transactional
    void createSubcontent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);
        var returnedSubcontentDTO = om.readValue(
            restSubcontentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subcontentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubcontentDTO.class
        );

        // Validate the Subcontent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubcontent = subcontentMapper.toEntity(returnedSubcontentDTO);
        assertSubcontentUpdatableFieldsEquals(returnedSubcontent, getPersistedSubcontent(returnedSubcontent));

        insertedSubcontent = returnedSubcontent;
    }

    @Test
    @Transactional
    void createSubcontentWithExistingId() throws Exception {
        // Create the Subcontent with an existing ID
        subcontent.setId("existing_id");
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubcontentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subcontentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubcontents() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        // Get all the subcontentList
        restSubcontentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subcontent.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSubcontent() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        // Get the subcontent
        restSubcontentMockMvc
            .perform(get(ENTITY_API_URL_ID, subcontent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subcontent.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSubcontent() throws Exception {
        // Get the subcontent
        restSubcontentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubcontent() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subcontent
        Subcontent updatedSubcontent = subcontentRepository.findById(subcontent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubcontent are not directly saved in db
        em.detach(updatedSubcontent);
        updatedSubcontent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(updatedSubcontent);

        restSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subcontentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subcontentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubcontentToMatchAllProperties(updatedSubcontent);
    }

    @Test
    @Transactional
    void putNonExistingSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subcontentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subcontentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubcontentWithPatch() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subcontent using partial update
        Subcontent partialUpdatedSubcontent = new Subcontent();
        partialUpdatedSubcontent.setId(subcontent.getId());

        restSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubcontent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubcontent))
            )
            .andExpect(status().isOk());

        // Validate the Subcontent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubcontentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubcontent, subcontent),
            getPersistedSubcontent(subcontent)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubcontentWithPatch() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subcontent using partial update
        Subcontent partialUpdatedSubcontent = new Subcontent();
        partialUpdatedSubcontent.setId(subcontent.getId());

        partialUpdatedSubcontent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubcontent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubcontent))
            )
            .andExpect(status().isOk());

        // Validate the Subcontent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubcontentUpdatableFieldsEquals(partialUpdatedSubcontent, getPersistedSubcontent(partialUpdatedSubcontent));
    }

    @Test
    @Transactional
    void patchNonExistingSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subcontentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subcontentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubcontent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subcontent.setId(UUID.randomUUID().toString());

        // Create the Subcontent
        SubcontentDTO subcontentDTO = subcontentMapper.toDto(subcontent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcontentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subcontentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subcontent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubcontent() throws Exception {
        // Initialize the database
        insertedSubcontent = subcontentRepository.saveAndFlush(subcontent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subcontent
        restSubcontentMockMvc
            .perform(delete(ENTITY_API_URL_ID, subcontent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subcontentRepository.count();
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

    protected Subcontent getPersistedSubcontent(Subcontent subcontent) {
        return subcontentRepository.findById(subcontent.getId()).orElseThrow();
    }

    protected void assertPersistedSubcontentToMatchAllProperties(Subcontent expectedSubcontent) {
        assertSubcontentAllPropertiesEquals(expectedSubcontent, getPersistedSubcontent(expectedSubcontent));
    }

    protected void assertPersistedSubcontentToMatchUpdatableProperties(Subcontent expectedSubcontent) {
        assertSubcontentAllUpdatablePropertiesEquals(expectedSubcontent, getPersistedSubcontent(expectedSubcontent));
    }
}
