package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.repository.LocaliteRepository;
import com.mycompany.myapp.service.dto.LocaliteDTO;
import com.mycompany.myapp.service.mapper.LocaliteMapper;
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
 * Integration tests for the {@link LocaliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocaliteResourceIT {

    private static final String DEFAULT_NOM_LOCALITE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LOCALITE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocaliteRepository localiteRepository;

    @Autowired
    private LocaliteMapper localiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocaliteMockMvc;

    private Localite localite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createEntity(EntityManager em) {
        Localite localite = new Localite().nomLocalite(DEFAULT_NOM_LOCALITE).codePostal(DEFAULT_CODE_POSTAL);
        return localite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createUpdatedEntity(EntityManager em) {
        Localite localite = new Localite().nomLocalite(UPDATED_NOM_LOCALITE).codePostal(UPDATED_CODE_POSTAL);
        return localite;
    }

    @BeforeEach
    public void initTest() {
        localite = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalite() throws Exception {
        int databaseSizeBeforeCreate = localiteRepository.findAll().size();
        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isCreated());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate + 1);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNomLocalite()).isEqualTo(DEFAULT_NOM_LOCALITE);
        assertThat(testLocalite.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void createLocaliteWithExistingId() throws Exception {
        // Create the Localite with an existing ID
        localite.setId(1L);
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        int databaseSizeBeforeCreate = localiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocalites() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLocalite").value(hasItem(DEFAULT_NOM_LOCALITE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)));
    }

    @Test
    @Transactional
    void getLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get the localite
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL_ID, localite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localite.getId().intValue()))
            .andExpect(jsonPath("$.nomLocalite").value(DEFAULT_NOM_LOCALITE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL));
    }

    @Test
    @Transactional
    void getNonExistingLocalite() throws Exception {
        // Get the localite
        restLocaliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite
        Localite updatedLocalite = localiteRepository.findById(localite.getId()).get();
        // Disconnect from session so that the updates on updatedLocalite are not directly saved in db
        em.detach(updatedLocalite);
        updatedLocalite.nomLocalite(UPDATED_NOM_LOCALITE).codePostal(UPDATED_CODE_POSTAL);
        LocaliteDTO localiteDTO = localiteMapper.toDto(updatedLocalite);

        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNomLocalite()).isEqualTo(UPDATED_NOM_LOCALITE);
        assertThat(testLocalite.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void putNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.nomLocalite(UPDATED_NOM_LOCALITE);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNomLocalite()).isEqualTo(UPDATED_NOM_LOCALITE);
        assertThat(testLocalite.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
    }

    @Test
    @Transactional
    void fullUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.nomLocalite(UPDATED_NOM_LOCALITE).codePostal(UPDATED_CODE_POSTAL);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getNomLocalite()).isEqualTo(UPDATED_NOM_LOCALITE);
        assertThat(testLocalite.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void patchNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeDelete = localiteRepository.findAll().size();

        // Delete the localite
        restLocaliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, localite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
