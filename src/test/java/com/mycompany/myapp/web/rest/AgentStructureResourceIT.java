package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AgentStructure;
import com.mycompany.myapp.repository.AgentStructureRepository;
import com.mycompany.myapp.service.dto.AgentStructureDTO;
import com.mycompany.myapp.service.mapper.AgentStructureMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AgentStructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgentStructureResourceIT {

    private static final String ENTITY_API_URL = "/api/agent-structures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentStructureRepository agentStructureRepository;

    @Autowired
    private AgentStructureMapper agentStructureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgentStructureMockMvc;

    private AgentStructure agentStructure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentStructure createEntity(EntityManager em) {
        AgentStructure agentStructure = new AgentStructure();
        return agentStructure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentStructure createUpdatedEntity(EntityManager em) {
        AgentStructure agentStructure = new AgentStructure();
        return agentStructure;
    }

    @BeforeEach
    public void initTest() {
        agentStructure = createEntity(em);
    }

    @Test
    @Transactional
    void createAgentStructure() throws Exception {
        int databaseSizeBeforeCreate = agentStructureRepository.findAll().size();
        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);
        restAgentStructureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeCreate + 1);
        AgentStructure testAgentStructure = agentStructureList.get(agentStructureList.size() - 1);
    }

    @Test
    @Transactional
    void createAgentStructureWithExistingId() throws Exception {
        // Create the AgentStructure with an existing ID
        agentStructure.setId(1L);
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        int databaseSizeBeforeCreate = agentStructureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentStructureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgentStructures() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        // Get all the agentStructureList
        restAgentStructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentStructure.getId().intValue())));
    }

    @Test
    @Transactional
    void getAgentStructure() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        // Get the agentStructure
        restAgentStructureMockMvc
            .perform(get(ENTITY_API_URL_ID, agentStructure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agentStructure.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAgentStructure() throws Exception {
        // Get the agentStructure
        restAgentStructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgentStructure() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();

        // Update the agentStructure
        AgentStructure updatedAgentStructure = agentStructureRepository.findById(agentStructure.getId()).get();
        // Disconnect from session so that the updates on updatedAgentStructure are not directly saved in db
        em.detach(updatedAgentStructure);
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(updatedAgentStructure);

        restAgentStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentStructureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
        AgentStructure testAgentStructure = agentStructureList.get(agentStructureList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentStructureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgentStructureWithPatch() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();

        // Update the agentStructure using partial update
        AgentStructure partialUpdatedAgentStructure = new AgentStructure();
        partialUpdatedAgentStructure.setId(agentStructure.getId());

        restAgentStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentStructure))
            )
            .andExpect(status().isOk());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
        AgentStructure testAgentStructure = agentStructureList.get(agentStructureList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAgentStructureWithPatch() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();

        // Update the agentStructure using partial update
        AgentStructure partialUpdatedAgentStructure = new AgentStructure();
        partialUpdatedAgentStructure.setId(agentStructure.getId());

        restAgentStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentStructure))
            )
            .andExpect(status().isOk());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
        AgentStructure testAgentStructure = agentStructureList.get(agentStructureList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agentStructureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgentStructure() throws Exception {
        int databaseSizeBeforeUpdate = agentStructureRepository.findAll().size();
        agentStructure.setId(count.incrementAndGet());

        // Create the AgentStructure
        AgentStructureDTO agentStructureDTO = agentStructureMapper.toDto(agentStructure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentStructureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentStructureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentStructure in the database
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgentStructure() throws Exception {
        // Initialize the database
        agentStructureRepository.saveAndFlush(agentStructure);

        int databaseSizeBeforeDelete = agentStructureRepository.findAll().size();

        // Delete the agentStructure
        restAgentStructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, agentStructure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentStructure> agentStructureList = agentStructureRepository.findAll();
        assertThat(agentStructureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
