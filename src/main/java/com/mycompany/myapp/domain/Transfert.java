package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "transfert")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transfert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_transfert")
    private Instant dateTransfert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "categorie", "gestion" }, allowSetters = true)
    private Immobilisation immobilisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "localite" }, allowSetters = true)
    private Structure struture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transfert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTransfert() {
        return this.dateTransfert;
    }

    public Transfert dateTransfert(Instant dateTransfert) {
        this.setDateTransfert(dateTransfert);
        return this;
    }

    public void setDateTransfert(Instant dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    public Immobilisation getImmobilisation() {
        return this.immobilisation;
    }

    public void setImmobilisation(Immobilisation immobilisation) {
        this.immobilisation = immobilisation;
    }

    public Transfert immobilisation(Immobilisation immobilisation) {
        this.setImmobilisation(immobilisation);
        return this;
    }

    public Structure getStruture() {
        return this.struture;
    }

    public void setStruture(Structure structure) {
        this.struture = structure;
    }

    public Transfert struture(Structure structure) {
        this.setStruture(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transfert)) {
            return false;
        }
        return id != null && id.equals(((Transfert) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transfert{" +
            "id=" + getId() +
            ", dateTransfert='" + getDateTransfert() + "'" +
            "}";
    }
}
