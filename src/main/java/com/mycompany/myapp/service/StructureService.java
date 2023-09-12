package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.StructureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Structure}.
 */
public interface StructureService {
    /**
     * Save a structure.
     *
     * @param structureDTO the entity to save.
     * @return the persisted entity.
     */
    StructureDTO save(StructureDTO structureDTO);

    /**
     * Updates a structure.
     *
     * @param structureDTO the entity to update.
     * @return the persisted entity.
     */
    StructureDTO update(StructureDTO structureDTO);

    /**
     * Partially updates a structure.
     *
     * @param structureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StructureDTO> partialUpdate(StructureDTO structureDTO);

    /**
     * Get all the structures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StructureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" structure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StructureDTO> findOne(Long id);

    /**
     * Delete the "id" structure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
