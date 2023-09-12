package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgentAffecter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgentAffecter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentAffecterRepository extends JpaRepository<AgentAffecter, Long> {}
