package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Content;
import com.mycompany.myapp.repository.ContentRepository;
import com.mycompany.myapp.service.dto.ContentDTO;
import com.mycompany.myapp.service.mapper.ContentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Content}.
 */
@Service
@Transactional
public class ContentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;

    private final ContentMapper contentMapper;

    public ContentService(ContentRepository contentRepository, ContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
    }

    /**
     * Save a content.
     *
     * @param contentDTO the entity to save.
     * @return the persisted entity.
     */
    public ContentDTO save(ContentDTO contentDTO) {
        LOG.debug("Request to save Content : {}", contentDTO);
        Content content = contentMapper.toEntity(contentDTO);
        content = contentRepository.save(content);
        return contentMapper.toDto(content);
    }

    /**
     * Update a content.
     *
     * @param contentDTO the entity to save.
     * @return the persisted entity.
     */
    public ContentDTO update(ContentDTO contentDTO) {
        LOG.debug("Request to update Content : {}", contentDTO);
        Content content = contentMapper.toEntity(contentDTO);
        content = contentRepository.save(content);
        return contentMapper.toDto(content);
    }

    /**
     * Partially update a content.
     *
     * @param contentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContentDTO> partialUpdate(ContentDTO contentDTO) {
        LOG.debug("Request to partially update Content : {}", contentDTO);

        return contentRepository
            .findById(contentDTO.getId())
            .map(existingContent -> {
                contentMapper.partialUpdate(existingContent, contentDTO);

                return existingContent;
            })
            .map(contentRepository::save)
            .map(contentMapper::toDto);
    }

    /**
     * Get all the contents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Contents");
        return contentRepository.findAll(pageable).map(contentMapper::toDto);
    }

    /**
     * Get one content by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContentDTO> findOne(String id) {
        LOG.debug("Request to get Content : {}", id);
        return contentRepository.findById(id).map(contentMapper::toDto);
    }

    /**
     * Delete the content by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Content : {}", id);
        contentRepository.deleteById(id);
    }
}
