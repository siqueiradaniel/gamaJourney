package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SubcontentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Subcontent}.
 */
public interface SubcontentService {
    /**
     * Save a subcontent.
     *
     * @param subcontentDTO the entity to save.
     * @return the persisted entity.
     */
    SubcontentDTO save(SubcontentDTO subcontentDTO);

    /**
     * Updates a subcontent.
     *
     * @param subcontentDTO the entity to update.
     * @return the persisted entity.
     */
    SubcontentDTO update(SubcontentDTO subcontentDTO);

    /**
     * Partially updates a subcontent.
     *
     * @param subcontentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubcontentDTO> partialUpdate(SubcontentDTO subcontentDTO);

    /**
     * Get all the subcontents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubcontentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subcontent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubcontentDTO> findOne(String id);

    /**
     * Delete the "id" subcontent.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
