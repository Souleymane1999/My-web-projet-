package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gestion;
import com.mycompany.myapp.repository.GestionRepository;
import com.mycompany.myapp.service.dto.GestionDTO;
import com.mycompany.myapp.service.mapper.GestionMapper;
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
 * Integration tests for the {@link GestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GestionResourceIT {

    private static final String DEFAULT_TYPE_GESTION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_GESTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gestions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GestionRepository gestionRepository;

    @Autowired
    private GestionMapper gestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGestionMockMvc;

    private Gestion gestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gestion createEntity(EntityManager em) {
        Gestion gestion = new Gestion().typeGestion(DEFAULT_TYPE_GESTION);
        return gestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gestion createUpdatedEntity(EntityManager em) {
        Gestion gestion = new Gestion().typeGestion(UPDATED_TYPE_GESTION);
        return gestion;
    }

    @BeforeEach
    public void initTest() {
        gestion = createEntity(em);
    }

    @Test
    @Transactional
    void createGestion() throws Exception {
        int databaseSizeBeforeCreate = gestionRepository.findAll().size();
        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);
        restGestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestionDTO)))
            .andExpect(status().isCreated());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeCreate + 1);
        Gestion testGestion = gestionList.get(gestionList.size() - 1);
        assertThat(testGestion.getTypeGestion()).isEqualTo(DEFAULT_TYPE_GESTION);
    }

    @Test
    @Transactional
    void createGestionWithExistingId() throws Exception {
        // Create the Gestion with an existing ID
        gestion.setId(1L);
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        int databaseSizeBeforeCreate = gestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGestions() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        // Get all the gestionList
        restGestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeGestion").value(hasItem(DEFAULT_TYPE_GESTION)));
    }

    @Test
    @Transactional
    void getGestion() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        // Get the gestion
        restGestionMockMvc
            .perform(get(ENTITY_API_URL_ID, gestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gestion.getId().intValue()))
            .andExpect(jsonPath("$.typeGestion").value(DEFAULT_TYPE_GESTION));
    }

    @Test
    @Transactional
    void getNonExistingGestion() throws Exception {
        // Get the gestion
        restGestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGestion() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();

        // Update the gestion
        Gestion updatedGestion = gestionRepository.findById(gestion.getId()).get();
        // Disconnect from session so that the updates on updatedGestion are not directly saved in db
        em.detach(updatedGestion);
        updatedGestion.typeGestion(UPDATED_TYPE_GESTION);
        GestionDTO gestionDTO = gestionMapper.toDto(updatedGestion);

        restGestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
        Gestion testGestion = gestionList.get(gestionList.size() - 1);
        assertThat(testGestion.getTypeGestion()).isEqualTo(UPDATED_TYPE_GESTION);
    }

    @Test
    @Transactional
    void putNonExistingGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGestionWithPatch() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();

        // Update the gestion using partial update
        Gestion partialUpdatedGestion = new Gestion();
        partialUpdatedGestion.setId(gestion.getId());

        restGestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestion))
            )
            .andExpect(status().isOk());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
        Gestion testGestion = gestionList.get(gestionList.size() - 1);
        assertThat(testGestion.getTypeGestion()).isEqualTo(DEFAULT_TYPE_GESTION);
    }

    @Test
    @Transactional
    void fullUpdateGestionWithPatch() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();

        // Update the gestion using partial update
        Gestion partialUpdatedGestion = new Gestion();
        partialUpdatedGestion.setId(gestion.getId());

        partialUpdatedGestion.typeGestion(UPDATED_TYPE_GESTION);

        restGestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestion))
            )
            .andExpect(status().isOk());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
        Gestion testGestion = gestionList.get(gestionList.size() - 1);
        assertThat(testGestion.getTypeGestion()).isEqualTo(UPDATED_TYPE_GESTION);
    }

    @Test
    @Transactional
    void patchNonExistingGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGestion() throws Exception {
        int databaseSizeBeforeUpdate = gestionRepository.findAll().size();
        gestion.setId(count.incrementAndGet());

        // Create the Gestion
        GestionDTO gestionDTO = gestionMapper.toDto(gestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gestion in the database
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGestion() throws Exception {
        // Initialize the database
        gestionRepository.saveAndFlush(gestion);

        int databaseSizeBeforeDelete = gestionRepository.findAll().size();

        // Delete the gestion
        restGestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, gestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gestion> gestionList = gestionRepository.findAll();
        assertThat(gestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
