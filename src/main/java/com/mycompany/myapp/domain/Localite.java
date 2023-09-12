package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Localite.
 */
@Entity
@Table(name = "localite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Localite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_localite")
    private String nomLocalite;

    @Column(name = "code_postal")
    private String codePostal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Localite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLocalite() {
        return this.nomLocalite;
    }

    public Localite nomLocalite(String nomLocalite) {
        this.setNomLocalite(nomLocalite);
        return this;
    }

    public void setNomLocalite(String nomLocalite) {
        this.nomLocalite = nomLocalite;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Localite codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Localite)) {
            return false;
        }
        return id != null && id.equals(((Localite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Localite{" +
            "id=" + getId() +
            ", nomLocalite='" + getNomLocalite() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            "}";
    }
}
