package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Localite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocaliteDTO implements Serializable {

    private Long id;

    private String nomLocalite;

    private String codePostal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLocalite() {
        return nomLocalite;
    }

    public void setNomLocalite(String nomLocalite) {
        this.nomLocalite = nomLocalite;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocaliteDTO)) {
            return false;
        }

        LocaliteDTO localiteDTO = (LocaliteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocaliteDTO{" +
            "id=" + getId() +
            ", nomLocalite='" + getNomLocalite() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            "}";
    }
}
