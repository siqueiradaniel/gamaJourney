package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LearningPathAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LearningPath;
import com.mycompany.myapp.repository.LearningPathRepository;
import com.mycompany.myapp.service.dto.LearningPathDTO;
import com.mycompany.myapp.service.mapper.LearningPathMapper;
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
 * Integration tests for the {@link LearningPathResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LearningPathResourceIT {

    private static final String DEFAULT_EXAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXAM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SUBCONTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUBCONTENT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/learning-paths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private LearningPathMapper learningPathMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLearningPathMockMvc;

    private LearningPath learningPath;

    private LearningPath insertedLearningPath;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LearningPath createEntity() {
        return new LearningPath().examId(DEFAULT_EXAM_ID).subcontentId(DEFAULT_SUBCONTENT_ID).order(DEFAULT_ORDER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LearningPath createUpdatedEntity() {
        return new LearningPath().examId(UPDATED_EXAM_ID).subcontentId(UPDATED_SUBCONTENT_ID).order(UPDATED_ORDER);
    }

    @BeforeEach
    public void initTest() {
        learningPath = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLearningPath != null) {
            learningPathRepository.delete(insertedLearningPath);
            insertedLearningPath = null;
        }
    }

    @Test
    @Transactional
    void createLearningPath() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);
        var returnedLearningPathDTO = om.readValue(
            restLearningPathMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(learningPathDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LearningPathDTO.class
        );

        // Validate the LearningPath in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLearningPath = learningPathMapper.toEntity(returnedLearningPathDTO);
        assertLearningPathUpdatableFieldsEquals(returnedLearningPath, getPersistedLearningPath(returnedLearningPath));

        insertedLearningPath = returnedLearningPath;
    }

    @Test
    @Transactional
    void createLearningPathWithExistingId() throws Exception {
        // Create the LearningPath with an existing ID
        learningPath.setId(1L);
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearningPathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(learningPathDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLearningPaths() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        // Get all the learningPathList
        restLearningPathMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learningPath.getId().intValue())))
            .andExpect(jsonPath("$.[*].examId").value(hasItem(DEFAULT_EXAM_ID)))
            .andExpect(jsonPath("$.[*].subcontentId").value(hasItem(DEFAULT_SUBCONTENT_ID)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    void getLearningPath() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        // Get the learningPath
        restLearningPathMockMvc
            .perform(get(ENTITY_API_URL_ID, learningPath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(learningPath.getId().intValue()))
            .andExpect(jsonPath("$.examId").value(DEFAULT_EXAM_ID))
            .andExpect(jsonPath("$.subcontentId").value(DEFAULT_SUBCONTENT_ID))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingLearningPath() throws Exception {
        // Get the learningPath
        restLearningPathMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLearningPath() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the learningPath
        LearningPath updatedLearningPath = learningPathRepository.findById(learningPath.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLearningPath are not directly saved in db
        em.detach(updatedLearningPath);
        updatedLearningPath.examId(UPDATED_EXAM_ID).subcontentId(UPDATED_SUBCONTENT_ID).order(UPDATED_ORDER);
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(updatedLearningPath);

        restLearningPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, learningPathDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(learningPathDTO))
            )
            .andExpect(status().isOk());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLearningPathToMatchAllProperties(updatedLearningPath);
    }

    @Test
    @Transactional
    void putNonExistingLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, learningPathDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(learningPathDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(learningPathDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(learningPathDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLearningPathWithPatch() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the learningPath using partial update
        LearningPath partialUpdatedLearningPath = new LearningPath();
        partialUpdatedLearningPath.setId(learningPath.getId());

        partialUpdatedLearningPath.examId(UPDATED_EXAM_ID).subcontentId(UPDATED_SUBCONTENT_ID);

        restLearningPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLearningPath.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLearningPath))
            )
            .andExpect(status().isOk());

        // Validate the LearningPath in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLearningPathUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLearningPath, learningPath),
            getPersistedLearningPath(learningPath)
        );
    }

    @Test
    @Transactional
    void fullUpdateLearningPathWithPatch() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the learningPath using partial update
        LearningPath partialUpdatedLearningPath = new LearningPath();
        partialUpdatedLearningPath.setId(learningPath.getId());

        partialUpdatedLearningPath.examId(UPDATED_EXAM_ID).subcontentId(UPDATED_SUBCONTENT_ID).order(UPDATED_ORDER);

        restLearningPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLearningPath.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLearningPath))
            )
            .andExpect(status().isOk());

        // Validate the LearningPath in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLearningPathUpdatableFieldsEquals(partialUpdatedLearningPath, getPersistedLearningPath(partialUpdatedLearningPath));
    }

    @Test
    @Transactional
    void patchNonExistingLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, learningPathDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(learningPathDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(learningPathDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLearningPath() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        learningPath.setId(longCount.incrementAndGet());

        // Create the LearningPath
        LearningPathDTO learningPathDTO = learningPathMapper.toDto(learningPath);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLearningPathMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(learningPathDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LearningPath in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLearningPath() throws Exception {
        // Initialize the database
        insertedLearningPath = learningPathRepository.saveAndFlush(learningPath);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the learningPath
        restLearningPathMockMvc
            .perform(delete(ENTITY_API_URL_ID, learningPath.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return learningPathRepository.count();
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

    protected LearningPath getPersistedLearningPath(LearningPath learningPath) {
        return learningPathRepository.findById(learningPath.getId()).orElseThrow();
    }

    protected void assertPersistedLearningPathToMatchAllProperties(LearningPath expectedLearningPath) {
        assertLearningPathAllPropertiesEquals(expectedLearningPath, getPersistedLearningPath(expectedLearningPath));
    }

    protected void assertPersistedLearningPathToMatchUpdatableProperties(LearningPath expectedLearningPath) {
        assertLearningPathAllUpdatablePropertiesEquals(expectedLearningPath, getPersistedLearningPath(expectedLearningPath));
    }
}
