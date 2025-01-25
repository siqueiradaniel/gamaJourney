package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DependencyDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Dependency}.
 */
public interface DependencyService {
    /**
     * Save a dependency.
     *
     * @param dependencyDTO the entity to save.
     * @return the persisted entity.
     */
    DependencyDTO save(DependencyDTO dependencyDTO);

    /**
     * Updates a dependency.
     *
     * @param dependencyDTO the entity to update.
     * @return the persisted entity.
     */
    DependencyDTO update(DependencyDTO dependencyDTO);

    /**
     * Partially updates a dependency.
     *
     * @param dependencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DependencyDTO> partialUpdate(DependencyDTO dependencyDTO);

    /**
     * Get all the dependencies.
     *
     * @return the list of entities.
     */
    List<DependencyDTO> findAll();

    /**
     * Get the "id" dependency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DependencyDTO> findOne(Long id);

    /**
     * Delete the "id" dependency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
