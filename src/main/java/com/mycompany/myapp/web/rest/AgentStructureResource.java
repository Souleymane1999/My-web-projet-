package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AgentStructureRepository;
import com.mycompany.myapp.service.AgentStructureService;
import com.mycompany.myapp.service.dto.AgentStructureDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AgentStructure}.
 */
@RestController
@RequestMapping("/api")
public class AgentStructureResource {

    private final Logger log = LoggerFactory.getLogger(AgentStructureResource.class);

    private static final String ENTITY_NAME = "agentStructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentStructureService agentStructureService;

    private final AgentStructureRepository agentStructureRepository;

    public AgentStructureResource(AgentStructureService agentStructureService, AgentStructureRepository agentStructureRepository) {
        this.agentStructureService = agentStructureService;
        this.agentStructureRepository = agentStructureRepository;
    }

    /**
     * {@code POST  /agent-structures} : Create a new agentStructure.
     *
     * @param agentStructureDTO the agentStructureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentStructureDTO, or with status {@code 400 (Bad Request)} if the agentStructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-structures")
    public ResponseEntity<AgentStructureDTO> createAgentStructure(@RequestBody AgentStructureDTO agentStructureDTO)
        throws URISyntaxException {
        log.debug("REST request to save AgentStructure : {}", agentStructureDTO);
        if (agentStructureDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentStructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentStructureDTO result = agentStructureService.save(agentStructureDTO);
        return ResponseEntity
            .created(new URI("/api/agent-structures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-structures/:id} : Updates an existing agentStructure.
     *
     * @param id the id of the agentStructureDTO to save.
     * @param agentStructureDTO the agentStructureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentStructureDTO,
     * or with status {@code 400 (Bad Request)} if the agentStructureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentStructureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-structures/{id}")
    public ResponseEntity<AgentStructureDTO> updateAgentStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgentStructureDTO agentStructureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgentStructure : {}, {}", id, agentStructureDTO);
        if (agentStructureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentStructureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgentStructureDTO result = agentStructureService.update(agentStructureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agentStructureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agent-structures/:id} : Partial updates given fields of an existing agentStructure, field will ignore if it is null
     *
     * @param id the id of the agentStructureDTO to save.
     * @param agentStructureDTO the agentStructureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentStructureDTO,
     * or with status {@code 400 (Bad Request)} if the agentStructureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agentStructureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agentStructureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agent-structures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgentStructureDTO> partialUpdateAgentStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgentStructureDTO agentStructureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgentStructure partially : {}, {}", id, agentStructureDTO);
        if (agentStructureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentStructureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgentStructureDTO> result = agentStructureService.partialUpdate(agentStructureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agentStructureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agent-structures} : get all the agentStructures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentStructures in body.
     */
    @GetMapping("/agent-structures")
    public ResponseEntity<List<AgentStructureDTO>> getAllAgentStructures(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AgentStructures");
        Page<AgentStructureDTO> page = agentStructureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agent-structures/:id} : get the "id" agentStructure.
     *
     * @param id the id of the agentStructureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentStructureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-structures/{id}")
    public ResponseEntity<AgentStructureDTO> getAgentStructure(@PathVariable Long id) {
        log.debug("REST request to get AgentStructure : {}", id);
        Optional<AgentStructureDTO> agentStructureDTO = agentStructureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentStructureDTO);
    }

    /**
     * {@code DELETE  /agent-structures/:id} : delete the "id" agentStructure.
     *
     * @param id the id of the agentStructureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-structures/{id}")
    public ResponseEntity<Void> deleteAgentStructure(@PathVariable Long id) {
        log.debug("REST request to delete AgentStructure : {}", id);
        agentStructureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
