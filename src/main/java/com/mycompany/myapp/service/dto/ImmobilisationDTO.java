package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Immobilisation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImmobilisationDTO implements Serializable {

    private Long id;

    private String nom;

    private String description;

    private String valeur;

    private String etat;

    private String quantite;

    private Instant dateAcquisition;

    private String typeAmortissement;

    private String dureeAmortissement;

    private CategorieDTO categorie;

    private GestionDTO gestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public Instant getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(Instant dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public String getTypeAmortissement() {
        return typeAmortissement;
    }

    public void setTypeAmortissement(String typeAmortissement) {
        this.typeAmortissement = typeAmortissement;
    }

    public String getDureeAmortissement() {
        return dureeAmortissement;
    }

    public void setDureeAmortissement(String dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public CategorieDTO getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDTO categorie) {
        this.categorie = categorie;
    }

    public GestionDTO getGestion() {
        return gestion;
    }

    public void setGestion(GestionDTO gestion) {
        this.gestion = gestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmobilisationDTO)) {
            return false;
        }

        ImmobilisationDTO immobilisationDTO = (ImmobilisationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, immobilisationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImmobilisationDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", valeur='" + getValeur() + "'" +
            ", etat='" + getEtat() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", dateAcquisition='" + getDateAcquisition() + "'" +
            ", typeAmortissement='" + getTypeAmortissement() + "'" +
            ", dureeAmortissement='" + getDureeAmortissement() + "'" +
            ", categorie=" + getCategorie() +
            ", gestion=" + getGestion() +
            "}";
    }
}
