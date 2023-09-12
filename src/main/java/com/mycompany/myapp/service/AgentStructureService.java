package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AgentStructureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AgentStructure}.
 */
public interface AgentStructureService {
    /**
     * Save a agentStructure.
     *
     * @param agentStructureDTO the entity to save.
     * @return the persisted entity.
     */
    AgentStructureDTO save(AgentStructureDTO agentStructureDTO);

    /**
     * Updates a agentStructure.
     *
     * @param agentStructureDTO the entity to update.
     * @return the persisted entity.
     */
    AgentStructureDTO update(AgentStructureDTO agentStructureDTO);

    /**
     * Partially updates a agentStructure.
     *
     * @param agentStructureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgentStructureDTO> partialUpdate(AgentStructureDTO agentStructureDTO);

    /**
     * Get all the agentStructures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgentStructureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" agentStructure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgentStructureDTO> findOne(Long id);

    /**
     * Delete the "id" agentStructure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
