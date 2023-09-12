package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Structure} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StructureDTO implements Serializable {

    private Long id;

    private String nomStructure;

    private String adresseStructure;

    private LocaliteDTO localite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomStructure() {
        return nomStructure;
    }

    public void setNomStructure(String nomStructure) {
        this.nomStructure = nomStructure;
    }

    public String getAdresseStructure() {
        return adresseStructure;
    }

    public void setAdresseStructure(String adresseStructure) {
        this.adresseStructure = adresseStructure;
    }

    public LocaliteDTO getLocalite() {
        return localite;
    }

    public void setLocalite(LocaliteDTO localite) {
        this.localite = localite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StructureDTO)) {
            return false;
        }

        StructureDTO structureDTO = (StructureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, structureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StructureDTO{" +
            "id=" + getId() +
            ", nomStructure='" + getNomStructure() + "'" +
            ", adresseStructure='" + getAdresseStructure() + "'" +
            ", localite=" + getLocalite() +
            "}";
    }
}
