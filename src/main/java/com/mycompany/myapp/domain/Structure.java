package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Structure.
 */
@Entity
@Table(name = "structure")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Structure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_structure")
    private String nomStructure;

    @Column(name = "adresse_structure")
    private String adresseStructure;

    @ManyToOne(fetch = FetchType.LAZY)
    private Localite localite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Structure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomStructure() {
        return this.nomStructure;
    }

    public Structure nomStructure(String nomStructure) {
        this.setNomStructure(nomStructure);
        return this;
    }

    public void setNomStructure(String nomStructure) {
        this.nomStructure = nomStructure;
    }

    public String getAdresseStructure() {
        return this.adresseStructure;
    }

    public Structure adresseStructure(String adresseStructure) {
        this.setAdresseStructure(adresseStructure);
        return this;
    }

    public void setAdresseStructure(String adresseStructure) {
        this.adresseStructure = adresseStructure;
    }

    public Localite getLocalite() {
        return this.localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    public Structure localite(Localite localite) {
        this.setLocalite(localite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Structure)) {
            return false;
        }
        return id != null && id.equals(((Structure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Structure{" +
            "id=" + getId() +
            ", nomStructure='" + getNomStructure() + "'" +
            ", adresseStructure='" + getAdresseStructure() + "'" +
            "}";
    }
}
