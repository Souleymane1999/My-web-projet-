package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Transfert;
import com.mycompany.myapp.repository.TransfertRepository;
import com.mycompany.myapp.service.dto.TransfertDTO;
import com.mycompany.myapp.service.mapper.TransfertMapper;
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
 * Integration tests for the {@link TransfertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransfertResourceIT {

    private static final Instant DEFAULT_DATE_TRANSFERT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TRANSFERT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transferts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransfertRepository transfertRepository;

    @Autowired
    private TransfertMapper transfertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransfertMockMvc;

    private Transfert transfert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfert createEntity(EntityManager em) {
        Transfert transfert = new Transfert().dateTransfert(DEFAULT_DATE_TRANSFERT);
        return transfert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfert createUpdatedEntity(EntityManager em) {
        Transfert transfert = new Transfert().dateTransfert(UPDATED_DATE_TRANSFERT);
        return transfert;
    }

    @BeforeEach
    public void initTest() {
        transfert = createEntity(em);
    }

    @Test
    @Transactional
    void createTransfert() throws Exception {
        int databaseSizeBeforeCreate = transfertRepository.findAll().size();
        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);
        restTransfertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transfertDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeCreate + 1);
        Transfert testTransfert = transfertList.get(transfertList.size() - 1);
        assertThat(testTransfert.getDateTransfert()).isEqualTo(DEFAULT_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void createTransfertWithExistingId() throws Exception {
        // Create the Transfert with an existing ID
        transfert.setId(1L);
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        int databaseSizeBeforeCreate = transfertRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transfertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransferts() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfert.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTransfert").value(hasItem(DEFAULT_DATE_TRANSFERT.toString())));
    }

    @Test
    @Transactional
    void getTransfert() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        // Get the transfert
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL_ID, transfert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfert.getId().intValue()))
            .andExpect(jsonPath("$.dateTransfert").value(DEFAULT_DATE_TRANSFERT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransfert() throws Exception {
        // Get the transfert
        restTransfertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransfert() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();

        // Update the transfert
        Transfert updatedTransfert = transfertRepository.findById(transfert.getId()).get();
        // Disconnect from session so that the updates on updatedTransfert are not directly saved in db
        em.detach(updatedTransfert);
        updatedTransfert.dateTransfert(UPDATED_DATE_TRANSFERT);
        TransfertDTO transfertDTO = transfertMapper.toDto(updatedTransfert);

        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
        Transfert testTransfert = transfertList.get(transfertList.size() - 1);
        assertThat(testTransfert.getDateTransfert()).isEqualTo(UPDATED_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void putNonExistingTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transfertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransfertWithPatch() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();

        // Update the transfert using partial update
        Transfert partialUpdatedTransfert = new Transfert();
        partialUpdatedTransfert.setId(transfert.getId());

        partialUpdatedTransfert.dateTransfert(UPDATED_DATE_TRANSFERT);

        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfert))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
        Transfert testTransfert = transfertList.get(transfertList.size() - 1);
        assertThat(testTransfert.getDateTransfert()).isEqualTo(UPDATED_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void fullUpdateTransfertWithPatch() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();

        // Update the transfert using partial update
        Transfert partialUpdatedTransfert = new Transfert();
        partialUpdatedTransfert.setId(transfert.getId());

        partialUpdatedTransfert.dateTransfert(UPDATED_DATE_TRANSFERT);

        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfert))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
        Transfert testTransfert = transfertList.get(transfertList.size() - 1);
        assertThat(testTransfert.getDateTransfert()).isEqualTo(UPDATED_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void patchNonExistingTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfert() throws Exception {
        int databaseSizeBeforeUpdate = transfertRepository.findAll().size();
        transfert.setId(count.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transfertDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfert in the database
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfert() throws Exception {
        // Initialize the database
        transfertRepository.saveAndFlush(transfert);

        int databaseSizeBeforeDelete = transfertRepository.findAll().size();

        // Delete the transfert
        restTransfertMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transfert> transfertList = transfertRepository.findAll();
        assertThat(transfertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
