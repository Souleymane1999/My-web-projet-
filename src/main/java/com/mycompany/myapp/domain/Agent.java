package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "agent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "nom_utilisateur")
    private String nomUtilisateur;

    @Column(name = "prenom_utilisateur")
    private String prenomUtilisateur;

    @Column(name = "poste")
    private String poste;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "telephone")
    private String telephone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Agent matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public Agent nomUtilisateur(String nomUtilisateur) {
        this.setNomUtilisateur(nomUtilisateur);
        return this;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return this.prenomUtilisateur;
    }

    public Agent prenomUtilisateur(String prenomUtilisateur) {
        this.setPrenomUtilisateur(prenomUtilisateur);
        return this;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public String getPoste() {
        return this.poste;
    }

    public Agent poste(String poste) {
        this.setPoste(poste);
        return this;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Agent adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Agent telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        return id != null && id.equals(((Agent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", prenomUtilisateur='" + getPrenomUtilisateur() + "'" +
            ", poste='" + getPoste() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
