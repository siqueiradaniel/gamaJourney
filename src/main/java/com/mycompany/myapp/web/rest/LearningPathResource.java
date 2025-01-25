package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LearningPathRepository;
import com.mycompany.myapp.service.LearningPathService;
import com.mycompany.myapp.service.dto.LearningPathDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LearningPath}.
 */
@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathResource {

    private static final Logger LOG = LoggerFactory.getLogger(LearningPathResource.class);

    private static final String ENTITY_NAME = "learningPath";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LearningPathService learningPathService;

    private final LearningPathRepository learningPathRepository;

    public LearningPathResource(LearningPathService learningPathService, LearningPathRepository learningPathRepository) {
        this.learningPathService = learningPathService;
        this.learningPathRepository = learningPathRepository;
    }

    /**
     * {@code POST  /learning-paths} : Create a new learningPath.
     *
     * @param learningPathDTO the learningPathDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new learningPathDTO, or with status {@code 400 (Bad Request)} if the learningPath has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LearningPathDTO> createLearningPath(@RequestBody LearningPathDTO learningPathDTO) throws URISyntaxException {
        LOG.debug("REST request to save LearningPath : {}", learningPathDTO);
        if (learningPathDTO.getId() != null) {
            throw new BadRequestAlertException("A new learningPath cannot already have an ID", ENTITY_NAME, "idexists");
        }
        learningPathDTO = learningPathService.save(learningPathDTO);
        return ResponseEntity.created(new URI("/api/learning-paths/" + learningPathDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, learningPathDTO.getId().toString()))
            .body(learningPathDTO);
    }

    /**
     * {@code PUT  /learning-paths/:id} : Updates an existing learningPath.
     *
     * @param id the id of the learningPathDTO to save.
     * @param learningPathDTO the learningPathDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated learningPathDTO,
     * or with status {@code 400 (Bad Request)} if the learningPathDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the learningPathDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LearningPathDTO> updateLearningPath(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LearningPathDTO learningPathDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LearningPath : {}, {}", id, learningPathDTO);
        if (learningPathDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, learningPathDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!learningPathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        learningPathDTO = learningPathService.update(learningPathDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, learningPathDTO.getId().toString()))
            .body(learningPathDTO);
    }

    /**
     * {@code PATCH  /learning-paths/:id} : Partial updates given fields of an existing learningPath, field will ignore if it is null
     *
     * @param id the id of the learningPathDTO to save.
     * @param learningPathDTO the learningPathDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated learningPathDTO,
     * or with status {@code 400 (Bad Request)} if the learningPathDTO is not valid,
     * or with status {@code 404 (Not Found)} if the learningPathDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the learningPathDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LearningPathDTO> partialUpdateLearningPath(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LearningPathDTO learningPathDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LearningPath partially : {}, {}", id, learningPathDTO);
        if (learningPathDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, learningPathDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!learningPathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LearningPathDTO> result = learningPathService.partialUpdate(learningPathDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, learningPathDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /learning-paths} : get all the learningPaths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of learningPaths in body.
     */
    @GetMapping("")
    public List<LearningPathDTO> getAllLearningPaths() {
        LOG.debug("REST request to get all LearningPaths");
        return learningPathService.findAll();
    }

    /**
     * {@code GET  /learning-paths/:id} : get the "id" learningPath.
     *
     * @param id the id of the learningPathDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the learningPathDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LearningPathDTO> getLearningPath(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LearningPath : {}", id);
        Optional<LearningPathDTO> learningPathDTO = learningPathService.findOne(id);
        return ResponseUtil.wrapOrNotFound(learningPathDTO);
    }

    /**
     * {@code DELETE  /learning-paths/:id} : delete the "id" learningPath.
     *
     * @param id the id of the learningPathDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPath(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LearningPath : {}", id);
        learningPathService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
