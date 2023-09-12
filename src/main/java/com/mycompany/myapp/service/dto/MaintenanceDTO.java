package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Maintenance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceDTO implements Serializable {

    private Long id;

    private Instant dateMaintenance;

    private String description;

    private String responsable;

    private String coutMaintenance;

    private ImmobilisationDTO immobilisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateMaintenance() {
        return dateMaintenance;
    }

    public void setDateMaintenance(Instant dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCoutMaintenance() {
        return coutMaintenance;
    }

    public void setCoutMaintenance(String coutMaintenance) {
        this.coutMaintenance = coutMaintenance;
    }

    public ImmobilisationDTO getImmobilisation() {
        return immobilisation;
    }

    public void setImmobilisation(ImmobilisationDTO immobilisation) {
        this.immobilisation = immobilisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintenanceDTO)) {
            return false;
        }

        MaintenanceDTO maintenanceDTO = (MaintenanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintenanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceDTO{" +
            "id=" + getId() +
            ", dateMaintenance='" + getDateMaintenance() + "'" +
            ", description='" + getDescription() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", coutMaintenance='" + getCoutMaintenance() + "'" +
            ", immobilisation=" + getImmobilisation() +
            "}";
    }
}
