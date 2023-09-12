package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ImmobilisationRepository;
import com.mycompany.myapp.service.ImmobilisationService;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Immobilisation}.
 */
@RestController
@RequestMapping("/api")
public class ImmobilisationResource {

    private final Logger log = LoggerFactory.getLogger(ImmobilisationResource.class);

    private static final String ENTITY_NAME = "immobilisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImmobilisationService immobilisationService;

    private final ImmobilisationRepository immobilisationRepository;

    public ImmobilisationResource(ImmobilisationService immobilisationService, ImmobilisationRepository immobilisationRepository) {
        this.immobilisationService = immobilisationService;
        this.immobilisationRepository = immobilisationRepository;
    }

    /**
     * {@code POST  /immobilisations} : Create a new immobilisation.
     *
     * @param immobilisationDTO the immobilisationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new immobilisationDTO, or with status {@code 400 (Bad Request)} if the immobilisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/immobilisations")
    public ResponseEntity<ImmobilisationDTO> createImmobilisation(@RequestBody ImmobilisationDTO immobilisationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Immobilisation : {}", immobilisationDTO);
        if (immobilisationDTO.getId() != null) {
            throw new BadRequestAlertException("A new immobilisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImmobilisationDTO result = immobilisationService.save(immobilisationDTO);
        return ResponseEntity
            .created(new URI("/api/immobilisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /immobilisations/:id} : Updates an existing immobilisation.
     *
     * @param id the id of the immobilisationDTO to save.
     * @param immobilisationDTO the immobilisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated immobilisationDTO,
     * or with status {@code 400 (Bad Request)} if the immobilisationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the immobilisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/immobilisations/{id}")
    public ResponseEntity<ImmobilisationDTO> updateImmobilisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImmobilisationDTO immobilisationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Immobilisation : {}, {}", id, immobilisationDTO);
        if (immobilisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, immobilisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!immobilisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImmobilisationDTO result = immobilisationService.update(immobilisationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, immobilisationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /immobilisations/:id} : Partial updates given fields of an existing immobilisation, field will ignore if it is null
     *
     * @param id the id of the immobilisationDTO to save.
     * @param immobilisationDTO the immobilisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated immobilisationDTO,
     * or with status {@code 400 (Bad Request)} if the immobilisationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the immobilisationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the immobilisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/immobilisations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImmobilisationDTO> partialUpdateImmobilisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImmobilisationDTO immobilisationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Immobilisation partially : {}, {}", id, immobilisationDTO);
        if (immobilisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, immobilisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!immobilisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImmobilisationDTO> result = immobilisationService.partialUpdate(immobilisationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, immobilisationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /immobilisations} : get all the immobilisations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of immobilisations in body.
     */
    @GetMapping("/immobilisations")
    public ResponseEntity<List<ImmobilisationDTO>> getAllImmobilisations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Immobilisations");
        Page<ImmobilisationDTO> page = immobilisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /immobilisations/:id} : get the "id" immobilisation.
     *
     * @param id the id of the immobilisationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the immobilisationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/immobilisations/{id}")
    public ResponseEntity<ImmobilisationDTO> getImmobilisation(@PathVariable Long id) {
        log.debug("REST request to get Immobilisation : {}", id);
        Optional<ImmobilisationDTO> immobilisationDTO = immobilisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(immobilisationDTO);
    }

    /**
     * {@code DELETE  /immobilisations/:id} : delete the "id" immobilisation.
     *
     * @param id the id of the immobilisationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/immobilisations/{id}")
    public ResponseEntity<Void> deleteImmobilisation(@PathVariable Long id) {
        log.debug("REST request to delete Immobilisation : {}", id);
        immobilisationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
