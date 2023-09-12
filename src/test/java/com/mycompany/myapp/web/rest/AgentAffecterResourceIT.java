package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AgentAffecter;
import com.mycompany.myapp.repository.AgentAffecterRepository;
import com.mycompany.myapp.service.dto.AgentAffecterDTO;
import com.mycompany.myapp.service.mapper.AgentAffecterMapper;
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
 * Integration tests for the {@link AgentAffecterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgentAffecterResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_POSTE = "AAAAAAAAAA";
    private static final String UPDATED_POSTE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agent-affecters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentAffecterRepository agentAffecterRepository;

    @Autowired
    private AgentAffecterMapper agentAffecterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgentAffecterMockMvc;

    private AgentAffecter agentAffecter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentAffecter createEntity(EntityManager em) {
        AgentAffecter agentAffecter = new AgentAffecter()
            .matricule(DEFAULT_MATRICULE)
            .nomUtilisateur(DEFAULT_NOM_UTILISATEUR)
            .prenomUtilisateur(DEFAULT_PRENOM_UTILISATEUR)
            .poste(DEFAULT_POSTE)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE);
        return agentAffecter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentAffecter createUpdatedEntity(EntityManager em) {
        AgentAffecter agentAffecter = new AgentAffecter()
            .matricule(UPDATED_MATRICULE)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .poste(UPDATED_POSTE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE);
        return agentAffecter;
    }

    @BeforeEach
    public void initTest() {
        agentAffecter = createEntity(em);
    }

    @Test
    @Transactional
    void createAgentAffecter() throws Exception {
        int databaseSizeBeforeCreate = agentAffecterRepository.findAll().size();
        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);
        restAgentAffecterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeCreate + 1);
        AgentAffecter testAgentAffecter = agentAffecterList.get(agentAffecterList.size() - 1);
        assertThat(testAgentAffecter.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testAgentAffecter.getNomUtilisateur()).isEqualTo(DEFAULT_NOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPrenomUtilisateur()).isEqualTo(DEFAULT_PRENOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPoste()).isEqualTo(DEFAULT_POSTE);
        assertThat(testAgentAffecter.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgentAffecter.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createAgentAffecterWithExistingId() throws Exception {
        // Create the AgentAffecter with an existing ID
        agentAffecter.setId(1L);
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        int databaseSizeBeforeCreate = agentAffecterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentAffecterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgentAffecters() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        // Get all the agentAffecterList
        restAgentAffecterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentAffecter.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nomUtilisateur").value(hasItem(DEFAULT_NOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].prenomUtilisateur").value(hasItem(DEFAULT_PRENOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getAgentAffecter() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        // Get the agentAffecter
        restAgentAffecterMockMvc
            .perform(get(ENTITY_API_URL_ID, agentAffecter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agentAffecter.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nomUtilisateur").value(DEFAULT_NOM_UTILISATEUR))
            .andExpect(jsonPath("$.prenomUtilisateur").value(DEFAULT_PRENOM_UTILISATEUR))
            .andExpect(jsonPath("$.poste").value(DEFAULT_POSTE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingAgentAffecter() throws Exception {
        // Get the agentAffecter
        restAgentAffecterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgentAffecter() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();

        // Update the agentAffecter
        AgentAffecter updatedAgentAffecter = agentAffecterRepository.findById(agentAffecter.getId()).get();
        // Disconnect from session so that the updates on updatedAgentAffecter are not directly saved in db
        em.detach(updatedAgentAffecter);
        updatedAgentAffecter
            .matricule(UPDATED_MATRICULE)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .poste(UPDATED_POSTE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE);
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(updatedAgentAffecter);

        restAgentAffecterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentAffecterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
        AgentAffecter testAgentAffecter = agentAffecterList.get(agentAffecterList.size() - 1);
        assertThat(testAgentAffecter.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgentAffecter.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPrenomUtilisateur()).isEqualTo(UPDATED_PRENOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPoste()).isEqualTo(UPDATED_POSTE);
        assertThat(testAgentAffecter.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgentAffecter.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentAffecterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgentAffecterWithPatch() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();

        // Update the agentAffecter using partial update
        AgentAffecter partialUpdatedAgentAffecter = new AgentAffecter();
        partialUpdatedAgentAffecter.setId(agentAffecter.getId());

        partialUpdatedAgentAffecter.matricule(UPDATED_MATRICULE).nomUtilisateur(UPDATED_NOM_UTILISATEUR).telephone(UPDATED_TELEPHONE);

        restAgentAffecterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentAffecter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentAffecter))
            )
            .andExpect(status().isOk());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
        AgentAffecter testAgentAffecter = agentAffecterList.get(agentAffecterList.size() - 1);
        assertThat(testAgentAffecter.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgentAffecter.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPrenomUtilisateur()).isEqualTo(DEFAULT_PRENOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPoste()).isEqualTo(DEFAULT_POSTE);
        assertThat(testAgentAffecter.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgentAffecter.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdateAgentAffecterWithPatch() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();

        // Update the agentAffecter using partial update
        AgentAffecter partialUpdatedAgentAffecter = new AgentAffecter();
        partialUpdatedAgentAffecter.setId(agentAffecter.getId());

        partialUpdatedAgentAffecter
            .matricule(UPDATED_MATRICULE)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .prenomUtilisateur(UPDATED_PRENOM_UTILISATEUR)
            .poste(UPDATED_POSTE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE);

        restAgentAffecterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentAffecter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentAffecter))
            )
            .andExpect(status().isOk());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
        AgentAffecter testAgentAffecter = agentAffecterList.get(agentAffecterList.size() - 1);
        assertThat(testAgentAffecter.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgentAffecter.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPrenomUtilisateur()).isEqualTo(UPDATED_PRENOM_UTILISATEUR);
        assertThat(testAgentAffecter.getPoste()).isEqualTo(UPDATED_POSTE);
        assertThat(testAgentAffecter.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgentAffecter.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agentAffecterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgentAffecter() throws Exception {
        int databaseSizeBeforeUpdate = agentAffecterRepository.findAll().size();
        agentAffecter.setId(count.incrementAndGet());

        // Create the AgentAffecter
        AgentAffecterDTO agentAffecterDTO = agentAffecterMapper.toDto(agentAffecter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentAffecterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentAffecterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentAffecter in the database
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgentAffecter() throws Exception {
        // Initialize the database
        agentAffecterRepository.saveAndFlush(agentAffecter);

        int databaseSizeBeforeDelete = agentAffecterRepository.findAll().size();

        // Delete the agentAffecter
        restAgentAffecterMockMvc
            .perform(delete(ENTITY_API_URL_ID, agentAffecter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentAffecter> agentAffecterList = agentAffecterRepository.findAll();
        assertThat(agentAffecterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
