package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Immobilisation.
 */
@Entity
@Table(name = "immobilisation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Immobilisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "valeur")
    private String valeur;

    @Column(name = "etat")
    private String etat;

    @Column(name = "quantite")
    private String quantite;

    @Column(name = "date_acquisition")
    private Instant dateAcquisition;

    @Column(name = "type_amortissement")
    private String typeAmortissement;

    @Column(name = "duree_amortissement")
    private String dureeAmortissement;

    @ManyToOne(fetch = FetchType.LAZY)
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gestion gestion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Immobilisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Immobilisation nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Immobilisation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Immobilisation valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getEtat() {
        return this.etat;
    }

    public Immobilisation etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getQuantite() {
        return this.quantite;
    }

    public Immobilisation quantite(String quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public Instant getDateAcquisition() {
        return this.dateAcquisition;
    }

    public Immobilisation dateAcquisition(Instant dateAcquisition) {
        this.setDateAcquisition(dateAcquisition);
        return this;
    }

    public void setDateAcquisition(Instant dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public String getTypeAmortissement() {
        return this.typeAmortissement;
    }

    public Immobilisation typeAmortissement(String typeAmortissement) {
        this.setTypeAmortissement(typeAmortissement);
        return this;
    }

    public void setTypeAmortissement(String typeAmortissement) {
        this.typeAmortissement = typeAmortissement;
    }

    public String getDureeAmortissement() {
        return this.dureeAmortissement;
    }

    public Immobilisation dureeAmortissement(String dureeAmortissement) {
        this.setDureeAmortissement(dureeAmortissement);
        return this;
    }

    public void setDureeAmortissement(String dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Immobilisation categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public Gestion getGestion() {
        return this.gestion;
    }

    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
    }

    public Immobilisation gestion(Gestion gestion) {
        this.setGestion(gestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Immobilisation)) {
            return false;
        }
        return id != null && id.equals(((Immobilisation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Immobilisation{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", valeur='" + getValeur() + "'" +
            ", etat='" + getEtat() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", dateAcquisition='" + getDateAcquisition() + "'" +
            ", typeAmortissement='" + getTypeAmortissement() + "'" +
            ", dureeAmortissement='" + getDureeAmortissement() + "'" +
            "}";
    }
}
