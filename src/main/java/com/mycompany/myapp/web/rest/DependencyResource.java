package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DependencyRepository;
import com.mycompany.myapp.service.DependencyService;
import com.mycompany.myapp.service.dto.DependencyDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Dependency}.
 */
@RestController
@RequestMapping("/api/dependencies")
public class DependencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(DependencyResource.class);

    private static final String ENTITY_NAME = "dependency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependencyService dependencyService;

    private final DependencyRepository dependencyRepository;

    public DependencyResource(DependencyService dependencyService, DependencyRepository dependencyRepository) {
        this.dependencyService = dependencyService;
        this.dependencyRepository = dependencyRepository;
    }

    /**
     * {@code POST  /dependencies} : Create a new dependency.
     *
     * @param dependencyDTO the dependencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependencyDTO, or with status {@code 400 (Bad Request)} if the dependency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DependencyDTO> createDependency(@RequestBody DependencyDTO dependencyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Dependency : {}", dependencyDTO);
        if (dependencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new dependency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dependencyDTO = dependencyService.save(dependencyDTO);
        return ResponseEntity.created(new URI("/api/dependencies/" + dependencyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dependencyDTO.getId().toString()))
            .body(dependencyDTO);
    }

    /**
     * {@code PUT  /dependencies/:id} : Updates an existing dependency.
     *
     * @param id the id of the dependencyDTO to save.
     * @param dependencyDTO the dependencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencyDTO,
     * or with status {@code 400 (Bad Request)} if the dependencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DependencyDTO> updateDependency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DependencyDTO dependencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Dependency : {}, {}", id, dependencyDTO);
        if (dependencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dependencyDTO = dependencyService.update(dependencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependencyDTO.getId().toString()))
            .body(dependencyDTO);
    }

    /**
     * {@code PATCH  /dependencies/:id} : Partial updates given fields of an existing dependency, field will ignore if it is null
     *
     * @param id the id of the dependencyDTO to save.
     * @param dependencyDTO the dependencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencyDTO,
     * or with status {@code 400 (Bad Request)} if the dependencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dependencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DependencyDTO> partialUpdateDependency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DependencyDTO dependencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Dependency partially : {}, {}", id, dependencyDTO);
        if (dependencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DependencyDTO> result = dependencyService.partialUpdate(dependencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dependencies} : get all the dependencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependencies in body.
     */
    @GetMapping("")
    public List<DependencyDTO> getAllDependencies() {
        LOG.debug("REST request to get all Dependencies");
        return dependencyService.findAll();
    }

    /**
     * {@code GET  /dependencies/:id} : get the "id" dependency.
     *
     * @param id the id of the dependencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DependencyDTO> getDependency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Dependency : {}", id);
        Optional<DependencyDTO> dependencyDTO = dependencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dependencyDTO);
    }

    /**
     * {@code DELETE  /dependencies/:id} : delete the "id" dependency.
     *
     * @param id the id of the dependencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDependency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Dependency : {}", id);
        dependencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
