package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PendingStudentSubcontent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PendingStudentSubcontent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PendingStudentSubcontentRepository extends JpaRepository<PendingStudentSubcontent, Long> {}
