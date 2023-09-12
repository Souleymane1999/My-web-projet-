package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Gestion.
 */
@Entity
@Table(name = "gestion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_gestion")
    private String typeGestion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeGestion() {
        return this.typeGestion;
    }

    public Gestion typeGestion(String typeGestion) {
        this.setTypeGestion(typeGestion);
        return this;
    }

    public void setTypeGestion(String typeGestion) {
        this.typeGestion = typeGestion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gestion)) {
            return false;
        }
        return id != null && id.equals(((Gestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gestion{" +
            "id=" + getId() +
            ", typeGestion='" + getTypeGestion() + "'" +
            "}";
    }
}
