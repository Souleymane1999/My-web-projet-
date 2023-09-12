package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Immobilisation}.
 */
public interface ImmobilisationService {
    /**
     * Save a immobilisation.
     *
     * @param immobilisationDTO the entity to save.
     * @return the persisted entity.
     */
    ImmobilisationDTO save(ImmobilisationDTO immobilisationDTO);

    /**
     * Updates a immobilisation.
     *
     * @param immobilisationDTO the entity to update.
     * @return the persisted entity.
     */
    ImmobilisationDTO update(ImmobilisationDTO immobilisationDTO);

    /**
     * Partially updates a immobilisation.
     *
     * @param immobilisationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImmobilisationDTO> partialUpdate(ImmobilisationDTO immobilisationDTO);

    /**
     * Get all the immobilisations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImmobilisationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" immobilisation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImmobilisationDTO> findOne(Long id);

    /**
     * Delete the "id" immobilisation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
