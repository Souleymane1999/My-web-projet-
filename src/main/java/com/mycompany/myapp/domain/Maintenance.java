package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Maintenance.
 */
@Entity
@Table(name = "maintenance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_maintenance")
    private Instant dateMaintenance;

    @Column(name = "description")
    private String description;

    @Column(name = "responsable")
    private String responsable;

    @Column(name = "cout_maintenance")
    private String coutMaintenance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "categorie", "gestion" }, allowSetters = true)
    private Immobilisation immobilisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maintenance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateMaintenance() {
        return this.dateMaintenance;
    }

    public Maintenance dateMaintenance(Instant dateMaintenance) {
        this.setDateMaintenance(dateMaintenance);
        return this;
    }

    public void setDateMaintenance(Instant dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
    }

    public String getDescription() {
        return this.description;
    }

    public Maintenance description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public Maintenance responsable(String responsable) {
        this.setResponsable(responsable);
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCoutMaintenance() {
        return this.coutMaintenance;
    }

    public Maintenance coutMaintenance(String coutMaintenance) {
        this.setCoutMaintenance(coutMaintenance);
        return this;
    }

    public void setCoutMaintenance(String coutMaintenance) {
        this.coutMaintenance = coutMaintenance;
    }

    public Immobilisation getImmobilisation() {
        return this.immobilisation;
    }

    public void setImmobilisation(Immobilisation immobilisation) {
        this.immobilisation = immobilisation;
    }

    public Maintenance immobilisation(Immobilisation immobilisation) {
        this.setImmobilisation(immobilisation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maintenance)) {
            return false;
        }
        return id != null && id.equals(((Maintenance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maintenance{" +
            "id=" + getId() +
            ", dateMaintenance='" + getDateMaintenance() + "'" +
            ", description='" + getDescription() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", coutMaintenance='" + getCoutMaintenance() + "'" +
            "}";
    }
}
