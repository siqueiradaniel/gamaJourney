package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ContentRepository;
import com.mycompany.myapp.service.ContentService;
import com.mycompany.myapp.service.dto.ContentDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Content}.
 */
@RestController
@RequestMapping("/api/contents")
public class ContentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContentResource.class);

    private static final String ENTITY_NAME = "content";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentService contentService;

    private final ContentRepository contentRepository;

    public ContentResource(ContentService contentService, ContentRepository contentRepository) {
        this.contentService = contentService;
        this.contentRepository = contentRepository;
    }

    /**
     * {@code POST  /contents} : Create a new content.
     *
     * @param contentDTO the contentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentDTO, or with status {@code 400 (Bad Request)} if the content has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContentDTO> createContent(@RequestBody ContentDTO contentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Content : {}", contentDTO);
        if (contentDTO.getId() != null) {
            throw new BadRequestAlertException("A new content cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contentDTO = contentService.save(contentDTO);
        return ResponseEntity.created(new URI("/api/contents/" + contentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contentDTO.getId()))
            .body(contentDTO);
    }

    /**
     * {@code PUT  /contents/:id} : Updates an existing content.
     *
     * @param id the id of the contentDTO to save.
     * @param contentDTO the contentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentDTO,
     * or with status {@code 400 (Bad Request)} if the contentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentDTO> updateContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContentDTO contentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Content : {}, {}", id, contentDTO);
        if (contentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contentDTO = contentService.update(contentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentDTO.getId()))
            .body(contentDTO);
    }

    /**
     * {@code PATCH  /contents/:id} : Partial updates given fields of an existing content, field will ignore if it is null
     *
     * @param id the id of the contentDTO to save.
     * @param contentDTO the contentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentDTO,
     * or with status {@code 400 (Bad Request)} if the contentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentDTO> partialUpdateContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContentDTO contentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Content partially : {}, {}", id, contentDTO);
        if (contentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentDTO> result = contentService.partialUpdate(contentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentDTO.getId())
        );
    }

    /**
     * {@code GET  /contents} : get all the contents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContentDTO>> getAllContents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Contents");
        Page<ContentDTO> page = contentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contents/:id} : get the "id" content.
     *
     * @param id the id of the contentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContent(@PathVariable("id") String id) {
        LOG.debug("REST request to get Content : {}", id);
        Optional<ContentDTO> contentDTO = contentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentDTO);
    }

    /**
     * {@code DELETE  /contents/:id} : delete the "id" content.
     *
     * @param id the id of the contentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Content : {}", id);
        contentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
