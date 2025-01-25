package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ExamAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Exam;
import com.mycompany.myapp.repository.ExamRepository;
import com.mycompany.myapp.service.dto.ExamDTO;
import com.mycompany.myapp.service.mapper.ExamMapper;
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
 * Integration tests for the {@link ExamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamMockMvc;

    private Exam exam;

    private Exam insertedExam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createEntity() {
        return new Exam().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createUpdatedEntity() {
        return new Exam().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        exam = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExam != null) {
            examRepository.delete(insertedExam);
            insertedExam = null;
        }
    }

    @Test
    @Transactional
    void createExam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);
        var returnedExamDTO = om.readValue(
            restExamMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExamDTO.class
        );

        // Validate the Exam in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExam = examMapper.toEntity(returnedExamDTO);
        assertExamUpdatableFieldsEquals(returnedExam, getPersistedExam(returnedExam));

        insertedExam = returnedExam;
    }

    @Test
    @Transactional
    void createExamWithExistingId() throws Exception {
        // Create the Exam with an existing ID
        exam.setId("existing_id");
        ExamDTO examDTO = examMapper.toDto(exam);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExams() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        // Get all the examList
        restExamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getExam() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        // Get the exam
        restExamMockMvc
            .perform(get(ENTITY_API_URL_ID, exam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exam.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExam() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExam are not directly saved in db
        em.detach(updatedExam);
        updatedExam.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ExamDTO examDTO = examMapper.toDto(updatedExam);

        restExamMockMvc
            .perform(put(ENTITY_API_URL_ID, examDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examDTO)))
            .andExpect(status().isOk());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExamToMatchAllProperties(updatedExam);
    }

    @Test
    @Transactional
    void putNonExistingExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(put(ENTITY_API_URL_ID, examDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamWithPatch() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exam using partial update
        Exam partialUpdatedExam = new Exam();
        partialUpdatedExam.setId(exam.getId());

        partialUpdatedExam.description(UPDATED_DESCRIPTION);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExam))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExam, exam), getPersistedExam(exam));
    }

    @Test
    @Transactional
    void fullUpdateExamWithPatch() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exam using partial update
        Exam partialUpdatedExam = new Exam();
        partialUpdatedExam.setId(exam.getId());

        partialUpdatedExam.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExam))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamUpdatableFieldsEquals(partialUpdatedExam, getPersistedExam(partialUpdatedExam));
    }

    @Test
    @Transactional
    void patchNonExistingExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exam.setId(UUID.randomUUID().toString());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExam() throws Exception {
        // Initialize the database
        insertedExam = examRepository.saveAndFlush(exam);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the exam
        restExamMockMvc
            .perform(delete(ENTITY_API_URL_ID, exam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examRepository.count();
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

    protected Exam getPersistedExam(Exam exam) {
        return examRepository.findById(exam.getId()).orElseThrow();
    }

    protected void assertPersistedExamToMatchAllProperties(Exam expectedExam) {
        assertExamAllPropertiesEquals(expectedExam, getPersistedExam(expectedExam));
    }

    protected void assertPersistedExamToMatchUpdatableProperties(Exam expectedExam) {
        assertExamAllUpdatablePropertiesEquals(expectedExam, getPersistedExam(expectedExam));
    }
}
