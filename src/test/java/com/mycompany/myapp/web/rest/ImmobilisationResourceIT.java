package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Immobilisation;
import com.mycompany.myapp.repository.ImmobilisationRepository;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import com.mycompany.myapp.service.mapper.ImmobilisationMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ImmobilisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImmobilisationResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITE = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ACQUISITION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACQUISITION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE_AMORTISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_AMORTISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE_AMORTISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_AMORTISSEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/immobilisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImmobilisationRepository immobilisationRepository;

    @Autowired
    private ImmobilisationMapper immobilisationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImmobilisationMockMvc;

    private Immobilisation immobilisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Immobilisation createEntity(EntityManager em) {
        Immobilisation immobilisation = new Immobilisation()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .valeur(DEFAULT_VALEUR)
            .etat(DEFAULT_ETAT)
            .quantite(DEFAULT_QUANTITE)
            .dateAcquisition(DEFAULT_DATE_ACQUISITION)
            .typeAmortissement(DEFAULT_TYPE_AMORTISSEMENT)
            .dureeAmortissement(DEFAULT_DUREE_AMORTISSEMENT);
        return immobilisation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Immobilisation createUpdatedEntity(EntityManager em) {
        Immobilisation immobilisation = new Immobilisation()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .valeur(UPDATED_VALEUR)
            .etat(UPDATED_ETAT)
            .quantite(UPDATED_QUANTITE)
            .dateAcquisition(UPDATED_DATE_ACQUISITION)
            .typeAmortissement(UPDATED_TYPE_AMORTISSEMENT)
            .dureeAmortissement(UPDATED_DUREE_AMORTISSEMENT);
        return immobilisation;
    }

    @BeforeEach
    public void initTest() {
        immobilisation = createEntity(em);
    }

    @Test
    @Transactional
    void createImmobilisation() throws Exception {
        int databaseSizeBeforeCreate = immobilisationRepository.findAll().size();
        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);
        restImmobilisationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeCreate + 1);
        Immobilisation testImmobilisation = immobilisationList.get(immobilisationList.size() - 1);
        assertThat(testImmobilisation.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testImmobilisation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImmobilisation.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testImmobilisation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testImmobilisation.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testImmobilisation.getDateAcquisition()).isEqualTo(DEFAULT_DATE_ACQUISITION);
        assertThat(testImmobilisation.getTypeAmortissement()).isEqualTo(DEFAULT_TYPE_AMORTISSEMENT);
        assertThat(testImmobilisation.getDureeAmortissement()).isEqualTo(DEFAULT_DUREE_AMORTISSEMENT);
    }

    @Test
    @Transactional
    void createImmobilisationWithExistingId() throws Exception {
        // Create the Immobilisation with an existing ID
        immobilisation.setId(1L);
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        int databaseSizeBeforeCreate = immobilisationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImmobilisationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImmobilisations() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        // Get all the immobilisationList
        restImmobilisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(immobilisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].dateAcquisition").value(hasItem(DEFAULT_DATE_ACQUISITION.toString())))
            .andExpect(jsonPath("$.[*].typeAmortissement").value(hasItem(DEFAULT_TYPE_AMORTISSEMENT)))
            .andExpect(jsonPath("$.[*].dureeAmortissement").value(hasItem(DEFAULT_DUREE_AMORTISSEMENT)));
    }

    @Test
    @Transactional
    void getImmobilisation() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        // Get the immobilisation
        restImmobilisationMockMvc
            .perform(get(ENTITY_API_URL_ID, immobilisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(immobilisation.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.dateAcquisition").value(DEFAULT_DATE_ACQUISITION.toString()))
            .andExpect(jsonPath("$.typeAmortissement").value(DEFAULT_TYPE_AMORTISSEMENT))
            .andExpect(jsonPath("$.dureeAmortissement").value(DEFAULT_DUREE_AMORTISSEMENT));
    }

    @Test
    @Transactional
    void getNonExistingImmobilisation() throws Exception {
        // Get the immobilisation
        restImmobilisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImmobilisation() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();

        // Update the immobilisation
        Immobilisation updatedImmobilisation = immobilisationRepository.findById(immobilisation.getId()).get();
        // Disconnect from session so that the updates on updatedImmobilisation are not directly saved in db
        em.detach(updatedImmobilisation);
        updatedImmobilisation
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .valeur(UPDATED_VALEUR)
            .etat(UPDATED_ETAT)
            .quantite(UPDATED_QUANTITE)
            .dateAcquisition(UPDATED_DATE_ACQUISITION)
            .typeAmortissement(UPDATED_TYPE_AMORTISSEMENT)
            .dureeAmortissement(UPDATED_DUREE_AMORTISSEMENT);
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(updatedImmobilisation);

        restImmobilisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, immobilisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
        Immobilisation testImmobilisation = immobilisationList.get(immobilisationList.size() - 1);
        assertThat(testImmobilisation.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testImmobilisation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImmobilisation.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testImmobilisation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testImmobilisation.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testImmobilisation.getDateAcquisition()).isEqualTo(UPDATED_DATE_ACQUISITION);
        assertThat(testImmobilisation.getTypeAmortissement()).isEqualTo(UPDATED_TYPE_AMORTISSEMENT);
        assertThat(testImmobilisation.getDureeAmortissement()).isEqualTo(UPDATED_DUREE_AMORTISSEMENT);
    }

    @Test
    @Transactional
    void putNonExistingImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, immobilisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImmobilisationWithPatch() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();

        // Update the immobilisation using partial update
        Immobilisation partialUpdatedImmobilisation = new Immobilisation();
        partialUpdatedImmobilisation.setId(immobilisation.getId());

        partialUpdatedImmobilisation
            .nom(UPDATED_NOM)
            .valeur(UPDATED_VALEUR)
            .quantite(UPDATED_QUANTITE)
            .dateAcquisition(UPDATED_DATE_ACQUISITION);

        restImmobilisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImmobilisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImmobilisation))
            )
            .andExpect(status().isOk());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
        Immobilisation testImmobilisation = immobilisationList.get(immobilisationList.size() - 1);
        assertThat(testImmobilisation.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testImmobilisation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImmobilisation.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testImmobilisation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testImmobilisation.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testImmobilisation.getDateAcquisition()).isEqualTo(UPDATED_DATE_ACQUISITION);
        assertThat(testImmobilisation.getTypeAmortissement()).isEqualTo(DEFAULT_TYPE_AMORTISSEMENT);
        assertThat(testImmobilisation.getDureeAmortissement()).isEqualTo(DEFAULT_DUREE_AMORTISSEMENT);
    }

    @Test
    @Transactional
    void fullUpdateImmobilisationWithPatch() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();

        // Update the immobilisation using partial update
        Immobilisation partialUpdatedImmobilisation = new Immobilisation();
        partialUpdatedImmobilisation.setId(immobilisation.getId());

        partialUpdatedImmobilisation
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .valeur(UPDATED_VALEUR)
            .etat(UPDATED_ETAT)
            .quantite(UPDATED_QUANTITE)
            .dateAcquisition(UPDATED_DATE_ACQUISITION)
            .typeAmortissement(UPDATED_TYPE_AMORTISSEMENT)
            .dureeAmortissement(UPDATED_DUREE_AMORTISSEMENT);

        restImmobilisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImmobilisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImmobilisation))
            )
            .andExpect(status().isOk());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
        Immobilisation testImmobilisation = immobilisationList.get(immobilisationList.size() - 1);
        assertThat(testImmobilisation.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testImmobilisation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImmobilisation.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testImmobilisation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testImmobilisation.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testImmobilisation.getDateAcquisition()).isEqualTo(UPDATED_DATE_ACQUISITION);
        assertThat(testImmobilisation.getTypeAmortissement()).isEqualTo(UPDATED_TYPE_AMORTISSEMENT);
        assertThat(testImmobilisation.getDureeAmortissement()).isEqualTo(UPDATED_DUREE_AMORTISSEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, immobilisationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImmobilisation() throws Exception {
        int databaseSizeBeforeUpdate = immobilisationRepository.findAll().size();
        immobilisation.setId(count.incrementAndGet());

        // Create the Immobilisation
        ImmobilisationDTO immobilisationDTO = immobilisationMapper.toDto(immobilisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImmobilisationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(immobilisationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Immobilisation in the database
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImmobilisation() throws Exception {
        // Initialize the database
        immobilisationRepository.saveAndFlush(immobilisation);

        int databaseSizeBeforeDelete = immobilisationRepository.findAll().size();

        // Delete the immobilisation
        restImmobilisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, immobilisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Immobilisation> immobilisationList = immobilisationRepository.findAll();
        assertThat(immobilisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
