package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PendingStudentSubcontentRepository;
import com.mycompany.myapp.service.PendingStudentSubcontentService;
import com.mycompany.myapp.service.dto.PendingStudentSubcontentDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PendingStudentSubcontent}.
 */
@RestController
@RequestMapping("/api/pending-student-subcontents")
public class PendingStudentSubcontentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PendingStudentSubcontentResource.class);

    private static final String ENTITY_NAME = "pendingStudentSubcontent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PendingStudentSubcontentService pendingStudentSubcontentService;

    private final PendingStudentSubcontentRepository pendingStudentSubcontentRepository;

    public PendingStudentSubcontentResource(
        PendingStudentSubcontentService pendingStudentSubcontentService,
        PendingStudentSubcontentRepository pendingStudentSubcontentRepository
    ) {
        this.pendingStudentSubcontentService = pendingStudentSubcontentService;
        this.pendingStudentSubcontentRepository = pendingStudentSubcontentRepository;
    }

    /**
     * {@code POST  /pending-student-subcontents} : Create a new pendingStudentSubcontent.
     *
     * @param pendingStudentSubcontentDTO the pendingStudentSubcontentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pendingStudentSubcontentDTO, or with status {@code 400 (Bad Request)} if the pendingStudentSubcontent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PendingStudentSubcontentDTO> createPendingStudentSubcontent(
        @Valid @RequestBody PendingStudentSubcontentDTO pendingStudentSubcontentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PendingStudentSubcontent : {}", pendingStudentSubcontentDTO);
        if (pendingStudentSubcontentDTO.getId() != null) {
            throw new BadRequestAlertException("A new pendingStudentSubcontent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pendingStudentSubcontentDTO = pendingStudentSubcontentService.save(pendingStudentSubcontentDTO);
        return ResponseEntity.created(new URI("/api/pending-student-subcontents/" + pendingStudentSubcontentDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pendingStudentSubcontentDTO.getId().toString())
            )
            .body(pendingStudentSubcontentDTO);
    }

    /**
     * {@code PUT  /pending-student-subcontents/:id} : Updates an existing pendingStudentSubcontent.
     *
     * @param id the id of the pendingStudentSubcontentDTO to save.
     * @param pendingStudentSubcontentDTO the pendingStudentSubcontentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pendingStudentSubcontentDTO,
     * or with status {@code 400 (Bad Request)} if the pendingStudentSubcontentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pendingStudentSubcontentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PendingStudentSubcontentDTO> updatePendingStudentSubcontent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PendingStudentSubcontentDTO pendingStudentSubcontentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PendingStudentSubcontent : {}, {}", id, pendingStudentSubcontentDTO);
        if (pendingStudentSubcontentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pendingStudentSubcontentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pendingStudentSubcontentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pendingStudentSubcontentDTO = pendingStudentSubcontentService.update(pendingStudentSubcontentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pendingStudentSubcontentDTO.getId().toString()))
            .body(pendingStudentSubcontentDTO);
    }

    /**
     * {@code PATCH  /pending-student-subcontents/:id} : Partial updates given fields of an existing pendingStudentSubcontent, field will ignore if it is null
     *
     * @param id the id of the pendingStudentSubcontentDTO to save.
     * @param pendingStudentSubcontentDTO the pendingStudentSubcontentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pendingStudentSubcontentDTO,
     * or with status {@code 400 (Bad Request)} if the pendingStudentSubcontentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pendingStudentSubcontentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pendingStudentSubcontentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PendingStudentSubcontentDTO> partialUpdatePendingStudentSubcontent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PendingStudentSubcontentDTO pendingStudentSubcontentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PendingStudentSubcontent partially : {}, {}", id, pendingStudentSubcontentDTO);
        if (pendingStudentSubcontentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pendingStudentSubcontentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pendingStudentSubcontentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PendingStudentSubcontentDTO> result = pendingStudentSubcontentService.partialUpdate(pendingStudentSubcontentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pendingStudentSubcontentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pending-student-subcontents} : get all the pendingStudentSubcontents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pendingStudentSubcontents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PendingStudentSubcontentDTO>> getAllPendingStudentSubcontents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PendingStudentSubcontents");
        Page<PendingStudentSubcontentDTO> page = pendingStudentSubcontentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pending-student-subcontents/:id} : get the "id" pendingStudentSubcontent.
     *
     * @param id the id of the pendingStudentSubcontentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pendingStudentSubcontentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PendingStudentSubcontentDTO> getPendingStudentSubcontent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PendingStudentSubcontent : {}", id);
        Optional<PendingStudentSubcontentDTO> pendingStudentSubcontentDTO = pendingStudentSubcontentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pendingStudentSubcontentDTO);
    }

    /**
     * {@code DELETE  /pending-student-subcontents/:id} : delete the "id" pendingStudentSubcontent.
     *
     * @param id the id of the pendingStudentSubcontentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePendingStudentSubcontent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PendingStudentSubcontent : {}", id);
        pendingStudentSubcontentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
