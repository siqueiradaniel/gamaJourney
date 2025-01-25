package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PendingStudentSubcontentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.PendingStudentSubcontent}.
 */
public interface PendingStudentSubcontentService {
    /**
     * Save a pendingStudentSubcontent.
     *
     * @param pendingStudentSubcontentDTO the entity to save.
     * @return the persisted entity.
     */
    PendingStudentSubcontentDTO save(PendingStudentSubcontentDTO pendingStudentSubcontentDTO);

    /**
     * Updates a pendingStudentSubcontent.
     *
     * @param pendingStudentSubcontentDTO the entity to update.
     * @return the persisted entity.
     */
    PendingStudentSubcontentDTO update(PendingStudentSubcontentDTO pendingStudentSubcontentDTO);

    /**
     * Partially updates a pendingStudentSubcontent.
     *
     * @param pendingStudentSubcontentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PendingStudentSubcontentDTO> partialUpdate(PendingStudentSubcontentDTO pendingStudentSubcontentDTO);

    /**
     * Get all the pendingStudentSubcontents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PendingStudentSubcontentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pendingStudentSubcontent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PendingStudentSubcontentDTO> findOne(Long id);

    /**
     * Delete the "id" pendingStudentSubcontent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
