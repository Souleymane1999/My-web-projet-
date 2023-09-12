package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Immobilisation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Immobilisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImmobilisationRepository extends JpaRepository<Immobilisation, Long> {}
