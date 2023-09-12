package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AgentAffecterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AgentAffecter}.
 */
public interface AgentAffecterService {
    /**
     * Save a agentAffecter.
     *
     * @param agentAffecterDTO the entity to save.
     * @return the persisted entity.
     */
    AgentAffecterDTO save(AgentAffecterDTO agentAffecterDTO);

    /**
     * Updates a agentAffecter.
     *
     * @param agentAffecterDTO the entity to update.
     * @return the persisted entity.
     */
    AgentAffecterDTO update(AgentAffecterDTO agentAffecterDTO);

    /**
     * Partially updates a agentAffecter.
     *
     * @param agentAffecterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgentAffecterDTO> partialUpdate(AgentAffecterDTO agentAffecterDTO);

    /**
     * Get all the agentAffecters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgentAffecterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" agentAffecter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgentAffecterDTO> findOne(Long id);

    /**
     * Delete the "id" agentAffecter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
