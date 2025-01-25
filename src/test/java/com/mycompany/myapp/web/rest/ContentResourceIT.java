package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ContentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Content;
import com.mycompany.myapp.repository.ContentRepository;
import com.mycompany.myapp.service.dto.ContentDTO;
import com.mycompany.myapp.service.mapper.ContentMapper;
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
 * Integration tests for the {@link ContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentMockMvc;

    private Content content;

    private Content insertedContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createEntity() {
        return new Content().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createUpdatedEntity() {
        return new Content().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        content = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContent != null) {
            contentRepository.delete(insertedContent);
            insertedContent = null;
        }
    }

    @Test
    @Transactional
    void createContent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);
        var returnedContentDTO = om.readValue(
            restContentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContentDTO.class
        );

        // Validate the Content in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContent = contentMapper.toEntity(returnedContentDTO);
        assertContentUpdatableFieldsEquals(returnedContent, getPersistedContent(returnedContent));

        insertedContent = returnedContent;
    }

    @Test
    @Transactional
    void createContentWithExistingId() throws Exception {
        // Create the Content with an existing ID
        content.setId("existing_id");
        ContentDTO contentDTO = contentMapper.toDto(content);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContents() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        // Get all the contentList
        restContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(content.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        // Get the content
        restContentMockMvc
            .perform(get(ENTITY_API_URL_ID, content.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(content.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingContent() throws Exception {
        // Get the content
        restContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content
        Content updatedContent = contentRepository.findById(content.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContent are not directly saved in db
        em.detach(updatedContent);
        updatedContent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ContentDTO contentDTO = contentMapper.toDto(updatedContent);

        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContentToMatchAllProperties(updatedContent);
    }

    @Test
    @Transactional
    void putNonExistingContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentWithPatch() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent.description(UPDATED_DESCRIPTION);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContent, content), getPersistedContent(content));
    }

    @Test
    @Transactional
    void fullUpdateContentWithPatch() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentUpdatableFieldsEquals(partialUpdatedContent, getPersistedContent(partialUpdatedContent));
    }

    @Test
    @Transactional
    void patchNonExistingContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(UUID.randomUUID().toString());

        // Create the Content
        ContentDTO contentDTO = contentMapper.toDto(content);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the content
        restContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, content.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contentRepository.count();
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

    protected Content getPersistedContent(Content content) {
        return contentRepository.findById(content.getId()).orElseThrow();
    }

    protected void assertPersistedContentToMatchAllProperties(Content expectedContent) {
        assertContentAllPropertiesEquals(expectedContent, getPersistedContent(expectedContent));
    }

    protected void assertPersistedContentToMatchUpdatableProperties(Content expectedContent) {
        assertContentAllUpdatablePropertiesEquals(expectedContent, getPersistedContent(expectedContent));
    }
}
