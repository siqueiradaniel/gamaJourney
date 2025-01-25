package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.LearningPathDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.LearningPath}.
 */
public interface LearningPathService {
    /**
     * Save a learningPath.
     *
     * @param learningPathDTO the entity to save.
     * @return the persisted entity.
     */
    LearningPathDTO save(LearningPathDTO learningPathDTO);

    /**
     * Updates a learningPath.
     *
     * @param learningPathDTO the entity to update.
     * @return the persisted entity.
     */
    LearningPathDTO update(LearningPathDTO learningPathDTO);

    /**
     * Partially updates a learningPath.
     *
     * @param learningPathDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LearningPathDTO> partialUpdate(LearningPathDTO learningPathDTO);

    /**
     * Get all the learningPaths.
     *
     * @return the list of entities.
     */
    List<LearningPathDTO> findAll();

    /**
     * Get the "id" learningPath.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LearningPathDTO> findOne(Long id);

    /**
     * Delete the "id" learningPath.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
