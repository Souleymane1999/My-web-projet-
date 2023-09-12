package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AgentAffecterRepository;
import com.mycompany.myapp.service.AgentAffecterService;
import com.mycompany.myapp.service.dto.AgentAffecterDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AgentAffecter}.
 */
@RestController
@RequestMapping("/api")
public class AgentAffecterResource {

    private final Logger log = LoggerFactory.getLogger(AgentAffecterResource.class);

    private static final String ENTITY_NAME = "agentAffecter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentAffecterService agentAffecterService;

    private final AgentAffecterRepository agentAffecterRepository;

    public AgentAffecterResource(AgentAffecterService agentAffecterService, AgentAffecterRepository agentAffecterRepository) {
        this.agentAffecterService = agentAffecterService;
        this.agentAffecterRepository = agentAffecterRepository;
    }

    /**
     * {@code POST  /agent-affecters} : Create a new agentAffecter.
     *
     * @param agentAffecterDTO the agentAffecterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentAffecterDTO, or with status {@code 400 (Bad Request)} if the agentAffecter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-affecters")
    public ResponseEntity<AgentAffecterDTO> createAgentAffecter(@RequestBody AgentAffecterDTO agentAffecterDTO) throws URISyntaxException {
        log.debug("REST request to save AgentAffecter : {}", agentAffecterDTO);
        if (agentAffecterDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentAffecter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentAffecterDTO result = agentAffecterService.save(agentAffecterDTO);
        return ResponseEntity
            .created(new URI("/api/agent-affecters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-affecters/:id} : Updates an existing agentAffecter.
     *
     * @param id the id of the agentAffecterDTO to save.
     * @param agentAffecterDTO the agentAffecterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentAffecterDTO,
     * or with status {@code 400 (Bad Request)} if the agentAffecterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentAffecterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-affecters/{id}")
    public ResponseEntity<AgentAffecterDTO> updateAgentAffecter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgentAffecterDTO agentAffecterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgentAffecter : {}, {}", id, agentAffecterDTO);
        if (agentAffecterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentAffecterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentAffecterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgentAffecterDTO result = agentAffecterService.update(agentAffecterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agentAffecterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agent-affecters/:id} : Partial updates given fields of an existing agentAffecter, field will ignore if it is null
     *
     * @param id the id of the agentAffecterDTO to save.
     * @param agentAffecterDTO the agentAffecterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentAffecterDTO,
     * or with status {@code 400 (Bad Request)} if the agentAffecterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agentAffecterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agentAffecterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agent-affecters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgentAffecterDTO> partialUpdateAgentAffecter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgentAffecterDTO agentAffecterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgentAffecter partially : {}, {}", id, agentAffecterDTO);
        if (agentAffecterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentAffecterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentAffecterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgentAffecterDTO> result = agentAffecterService.partialUpdate(agentAffecterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agentAffecterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agent-affecters} : get all the agentAffecters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentAffecters in body.
     */
    @GetMapping("/agent-affecters")
    public ResponseEntity<List<AgentAffecterDTO>> getAllAgentAffecters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AgentAffecters");
        Page<AgentAffecterDTO> page = agentAffecterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agent-affecters/:id} : get the "id" agentAffecter.
     *
     * @param id the id of the agentAffecterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentAffecterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-affecters/{id}")
    public ResponseEntity<AgentAffecterDTO> getAgentAffecter(@PathVariable Long id) {
        log.debug("REST request to get AgentAffecter : {}", id);
        Optional<AgentAffecterDTO> agentAffecterDTO = agentAffecterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentAffecterDTO);
    }

    /**
     * {@code DELETE  /agent-affecters/:id} : delete the "id" agentAffecter.
     *
     * @param id the id of the agentAffecterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-affecters/{id}")
    public ResponseEntity<Void> deleteAgentAffecter(@PathVariable Long id) {
        log.debug("REST request to delete AgentAffecter : {}", id);
        agentAffecterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
