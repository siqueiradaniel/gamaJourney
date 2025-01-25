package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Subcontent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Subcontent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubcontentRepository extends JpaRepository<Subcontent, String> {}
