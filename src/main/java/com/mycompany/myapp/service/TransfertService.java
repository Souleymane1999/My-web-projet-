package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TransfertDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Transfert}.
 */
public interface TransfertService {
    /**
     * Save a transfert.
     *
     * @param transfertDTO the entity to save.
     * @return the persisted entity.
     */
    TransfertDTO save(TransfertDTO transfertDTO);

    /**
     * Updates a transfert.
     *
     * @param transfertDTO the entity to update.
     * @return the persisted entity.
     */
    TransfertDTO update(TransfertDTO transfertDTO);

    /**
     * Partially updates a transfert.
     *
     * @param transfertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransfertDTO> partialUpdate(TransfertDTO transfertDTO);

    /**
     * Get all the transferts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransfertDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transfert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransfertDTO> findOne(Long id);

    /**
     * Delete the "id" transfert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
