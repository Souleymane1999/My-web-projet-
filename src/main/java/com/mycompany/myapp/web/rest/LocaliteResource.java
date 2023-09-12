package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LocaliteRepository;
import com.mycompany.myapp.service.LocaliteService;
import com.mycompany.myapp.service.dto.LocaliteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Localite}.
 */
@RestController
@RequestMapping("/api")
public class LocaliteResource {

    private final Logger log = LoggerFactory.getLogger(LocaliteResource.class);

    private static final String ENTITY_NAME = "localite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocaliteService localiteService;

    private final LocaliteRepository localiteRepository;

    public LocaliteResource(LocaliteService localiteService, LocaliteRepository localiteRepository) {
        this.localiteService = localiteService;
        this.localiteRepository = localiteRepository;
    }

    /**
     * {@code POST  /localites} : Create a new localite.
     *
     * @param localiteDTO the localiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localiteDTO, or with status {@code 400 (Bad Request)} if the localite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/localites")
    public ResponseEntity<LocaliteDTO> createLocalite(@RequestBody LocaliteDTO localiteDTO) throws URISyntaxException {
        log.debug("REST request to save Localite : {}", localiteDTO);
        if (localiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new localite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocaliteDTO result = localiteService.save(localiteDTO);
        return ResponseEntity
            .created(new URI("/api/localites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /localites/:id} : Updates an existing localite.
     *
     * @param id the id of the localiteDTO to save.
     * @param localiteDTO the localiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localiteDTO,
     * or with status {@code 400 (Bad Request)} if the localiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/localites/{id}")
    public ResponseEntity<LocaliteDTO> updateLocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocaliteDTO localiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Localite : {}, {}", id, localiteDTO);
        if (localiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocaliteDTO result = localiteService.update(localiteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /localites/:id} : Partial updates given fields of an existing localite, field will ignore if it is null
     *
     * @param id the id of the localiteDTO to save.
     * @param localiteDTO the localiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localiteDTO,
     * or with status {@code 400 (Bad Request)} if the localiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/localites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocaliteDTO> partialUpdateLocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocaliteDTO localiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Localite partially : {}, {}", id, localiteDTO);
        if (localiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocaliteDTO> result = localiteService.partialUpdate(localiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /localites} : get all the localites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localites in body.
     */
    @GetMapping("/localites")
    public ResponseEntity<List<LocaliteDTO>> getAllLocalites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Localites");
        Page<LocaliteDTO> page = localiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /localites/:id} : get the "id" localite.
     *
     * @param id the id of the localiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/localites/{id}")
    public ResponseEntity<LocaliteDTO> getLocalite(@PathVariable Long id) {
        log.debug("REST request to get Localite : {}", id);
        Optional<LocaliteDTO> localiteDTO = localiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localiteDTO);
    }

    /**
     * {@code DELETE  /localites/:id} : delete the "id" localite.
     *
     * @param id the id of the localiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/localites/{id}")
    public ResponseEntity<Void> deleteLocalite(@PathVariable Long id) {
        log.debug("REST request to delete Localite : {}", id);
        localiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
