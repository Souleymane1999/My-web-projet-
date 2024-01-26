package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Maintenance;
import com.mycompany.myapp.repository.MaintenanceRepository;
import com.mycompany.myapp.service.dto.MaintenanceDTO;
import com.mycompany.myapp.service.mapper.MaintenanceMapper;
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
 * Integration tests for the {@link MaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaintenanceResourceIT {

    private static final Instant DEFAULT_DATE_MAINTENANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MAINTENANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_COUT_MAINTENANCE = "AAAAAAAAAA";
    private static final String UPDATED_COUT_MAINTENANCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintenanceMockMvc;

    private Maintenance maintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .dateMaintenance(DEFAULT_DATE_MAINTENANCE)
            .description(DEFAULT_DESCRIPTION)
            .responsable(DEFAULT_RESPONSABLE)
            .coutMaintenance(DEFAULT_COUT_MAINTENANCE);
        return maintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createUpdatedEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .dateMaintenance(UPDATED_DATE_MAINTENANCE)
            .description(UPDATED_DESCRIPTION)
            .responsable(UPDATED_RESPONSABLE)
            .coutMaintenance(UPDATED_COUT_MAINTENANCE);
        return maintenance;
    }

    @BeforeEach
    public void initTest() {
        maintenance = createEntity(em);
    }

    @Test
    @Transactional
    void createMaintenance() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();
        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);
        restMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(DEFAULT_DATE_MAINTENANCE);
        assertThat(testMaintenance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaintenance.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testMaintenance.getCoutMaintenance()).isEqualTo(DEFAULT_COUT_MAINTENANCE);
    }

    @Test
    @Transactional
    void createMaintenanceWithExistingId() throws Exception {
        // Create the Maintenance with an existing ID
        maintenance.setId(1L);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaintenances() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateMaintenance").value(hasItem(DEFAULT_DATE_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].coutMaintenance").value(hasItem(DEFAULT_COUT_MAINTENANCE)));
    }

    @Test
    @Transactional
    void getMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get the maintenance
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, maintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintenance.getId().intValue()))
            .andExpect(jsonPath("$.dateMaintenance").value(DEFAULT_DATE_MAINTENANCE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.coutMaintenance").value(DEFAULT_COUT_MAINTENANCE));
    }

    @Test
    @Transactional
    void getNonExistingMaintenance() throws Exception {
        // Get the maintenance
        restMaintenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance
        Maintenance updatedMaintenance = maintenanceRepository.findById(maintenance.getId()).get();
        // Disconnect from session so that the updates on updatedMaintenance are not directly saved in db
        em.detach(updatedMaintenance);
        updatedMaintenance
            .dateMaintenance(UPDATED_DATE_MAINTENANCE)
            .description(UPDATED_DESCRIPTION)
            .responsable(UPDATED_RESPONSABLE)
            .coutMaintenance(UPDATED_COUT_MAINTENANCE);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(updatedMaintenance);

        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(UPDATED_DATE_MAINTENANCE);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testMaintenance.getCoutMaintenance()).isEqualTo(UPDATED_COUT_MAINTENANCE);
    }

    @Test
    @Transactional
    void putNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maintenanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintenanceWithPatch() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance using partial update
        Maintenance partialUpdatedMaintenance = new Maintenance();
        partialUpdatedMaintenance.setId(maintenance.getId());

        partialUpdatedMaintenance
            .dateMaintenance(UPDATED_DATE_MAINTENANCE)
            .responsable(UPDATED_RESPONSABLE)
            .coutMaintenance(UPDATED_COUT_MAINTENANCE);

        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(UPDATED_DATE_MAINTENANCE);
        assertThat(testMaintenance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaintenance.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testMaintenance.getCoutMaintenance()).isEqualTo(UPDATED_COUT_MAINTENANCE);
    }

    @Test
    @Transactional
    void fullUpdateMaintenanceWithPatch() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance using partial update
        Maintenance partialUpdatedMaintenance = new Maintenance();
        partialUpdatedMaintenance.setId(maintenance.getId());

        partialUpdatedMaintenance
            .dateMaintenance(UPDATED_DATE_MAINTENANCE)
            .description(UPDATED_DESCRIPTION)
            .responsable(UPDATED_RESPONSABLE)
            .coutMaintenance(UPDATED_COUT_MAINTENANCE);

        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(UPDATED_DATE_MAINTENANCE);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testMaintenance.getCoutMaintenance()).isEqualTo(UPDATED_COUT_MAINTENANCE);
    }

    @Test
    @Transactional
    void patchNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeDelete = maintenanceRepository.findAll().size();

        // Delete the maintenance
        restMaintenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintenance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
