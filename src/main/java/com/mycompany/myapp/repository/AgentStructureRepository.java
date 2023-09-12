package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgentStructure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgentStructure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentStructureRepository extends JpaRepository<AgentStructure, Long> {}
