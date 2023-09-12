package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TransfertRepository;
import com.mycompany.myapp.service.TransfertService;
import com.mycompany.myapp.service.dto.TransfertDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Transfert}.
 */
@RestController
@RequestMapping("/api")
public class TransfertResource {

    private final Logger log = LoggerFactory.getLogger(TransfertResource.class);

    private static final String ENTITY_NAME = "transfert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransfertService transfertService;

    private final TransfertRepository transfertRepository;

    public TransfertResource(TransfertService transfertService, TransfertRepository transfertRepository) {
        this.transfertService = transfertService;
        this.transfertRepository = transfertRepository;
    }

    /**
     * {@code POST  /transferts} : Create a new transfert.
     *
     * @param transfertDTO the transfertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transfertDTO, or with status {@code 400 (Bad Request)} if the transfert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transferts")
    public ResponseEntity<TransfertDTO> createTransfert(@RequestBody TransfertDTO transfertDTO) throws URISyntaxException {
        log.debug("REST request to save Transfert : {}", transfertDTO);
        if (transfertDTO.getId() != null) {
            throw new BadRequestAlertException("A new transfert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransfertDTO result = transfertService.save(transfertDTO);
        return ResponseEntity
            .created(new URI("/api/transferts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transferts/:id} : Updates an existing transfert.
     *
     * @param id the id of the transfertDTO to save.
     * @param transfertDTO the transfertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfertDTO,
     * or with status {@code 400 (Bad Request)} if the transfertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transfertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transferts/{id}")
    public ResponseEntity<TransfertDTO> updateTransfert(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfertDTO transfertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transfert : {}, {}", id, transfertDTO);
        if (transfertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransfertDTO result = transfertService.update(transfertDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfertDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transferts/:id} : Partial updates given fields of an existing transfert, field will ignore if it is null
     *
     * @param id the id of the transfertDTO to save.
     * @param transfertDTO the transfertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfertDTO,
     * or with status {@code 400 (Bad Request)} if the transfertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transfertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transfertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transferts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransfertDTO> partialUpdateTransfert(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfertDTO transfertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transfert partially : {}, {}", id, transfertDTO);
        if (transfertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransfertDTO> result = transfertService.partialUpdate(transfertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transferts} : get all the transferts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transferts in body.
     */
    @GetMapping("/transferts")
    public ResponseEntity<List<TransfertDTO>> getAllTransferts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transferts");
        Page<TransfertDTO> page = transfertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transferts/:id} : get the "id" transfert.
     *
     * @param id the id of the transfertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transfertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transferts/{id}")
    public ResponseEntity<TransfertDTO> getTransfert(@PathVariable Long id) {
        log.debug("REST request to get Transfert : {}", id);
        Optional<TransfertDTO> transfertDTO = transfertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transfertDTO);
    }

    /**
     * {@code DELETE  /transferts/:id} : delete the "id" transfert.
     *
     * @param id the id of the transfertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transferts/{id}")
    public ResponseEntity<Void> deleteTransfert(@PathVariable Long id) {
        log.debug("REST request to delete Transfert : {}", id);
        transfertService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
