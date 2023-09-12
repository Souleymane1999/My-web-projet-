package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.LocaliteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Localite}.
 */
public interface LocaliteService {
    /**
     * Save a localite.
     *
     * @param localiteDTO the entity to save.
     * @return the persisted entity.
     */
    LocaliteDTO save(LocaliteDTO localiteDTO);

    /**
     * Updates a localite.
     *
     * @param localiteDTO the entity to update.
     * @return the persisted entity.
     */
    LocaliteDTO update(LocaliteDTO localiteDTO);

    /**
     * Partially updates a localite.
     *
     * @param localiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocaliteDTO> partialUpdate(LocaliteDTO localiteDTO);

    /**
     * Get all the localites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocaliteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" localite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocaliteDTO> findOne(Long id);

    /**
     * Delete the "id" localite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
