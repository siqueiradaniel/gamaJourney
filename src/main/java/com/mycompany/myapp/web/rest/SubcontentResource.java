package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SubcontentRepository;
import com.mycompany.myapp.service.SubcontentService;
import com.mycompany.myapp.service.dto.SubcontentDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Subcontent}.
 */
@RestController
@RequestMapping("/api/subcontents")
public class SubcontentResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubcontentResource.class);

    private static final String ENTITY_NAME = "subcontent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubcontentService subcontentService;

    private final SubcontentRepository subcontentRepository;

    public SubcontentResource(SubcontentService subcontentService, SubcontentRepository subcontentRepository) {
        this.subcontentService = subcontentService;
        this.subcontentRepository = subcontentRepository;
    }

    /**
     * {@code POST  /subcontents} : Create a new subcontent.
     *
     * @param subcontentDTO the subcontentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subcontentDTO, or with status {@code 400 (Bad Request)} if the subcontent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubcontentDTO> createSubcontent(@RequestBody SubcontentDTO subcontentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Subcontent : {}", subcontentDTO);
        if (subcontentDTO.getId() != null) {
            throw new BadRequestAlertException("A new subcontent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subcontentDTO = subcontentService.save(subcontentDTO);
        return ResponseEntity.created(new URI("/api/subcontents/" + subcontentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subcontentDTO.getId()))
            .body(subcontentDTO);
    }

    /**
     * {@code PUT  /subcontents/:id} : Updates an existing subcontent.
     *
     * @param id the id of the subcontentDTO to save.
     * @param subcontentDTO the subcontentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subcontentDTO,
     * or with status {@code 400 (Bad Request)} if the subcontentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subcontentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubcontentDTO> updateSubcontent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SubcontentDTO subcontentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Subcontent : {}, {}", id, subcontentDTO);
        if (subcontentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subcontentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subcontentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subcontentDTO = subcontentService.update(subcontentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subcontentDTO.getId()))
            .body(subcontentDTO);
    }

    /**
     * {@code PATCH  /subcontents/:id} : Partial updates given fields of an existing subcontent, field will ignore if it is null
     *
     * @param id the id of the subcontentDTO to save.
     * @param subcontentDTO the subcontentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subcontentDTO,
     * or with status {@code 400 (Bad Request)} if the subcontentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subcontentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subcontentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubcontentDTO> partialUpdateSubcontent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SubcontentDTO subcontentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Subcontent partially : {}, {}", id, subcontentDTO);
        if (subcontentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subcontentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subcontentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubcontentDTO> result = subcontentService.partialUpdate(subcontentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subcontentDTO.getId())
        );
    }

    /**
     * {@code GET  /subcontents} : get all the subcontents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subcontents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubcontentDTO>> getAllSubcontents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Subcontents");
        Page<SubcontentDTO> page = subcontentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subcontents/:id} : get the "id" subcontent.
     *
     * @param id the id of the subcontentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subcontentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubcontentDTO> getSubcontent(@PathVariable("id") String id) {
        LOG.debug("REST request to get Subcontent : {}", id);
        Optional<SubcontentDTO> subcontentDTO = subcontentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subcontentDTO);
    }

    /**
     * {@code DELETE  /subcontents/:id} : delete the "id" subcontent.
     *
     * @param id the id of the subcontentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubcontent(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Subcontent : {}", id);
        subcontentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
