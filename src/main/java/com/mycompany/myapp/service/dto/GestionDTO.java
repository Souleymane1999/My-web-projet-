package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Gestion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionDTO implements Serializable {

    private Long id;

    private String typeGestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(String typeGestion) {
        this.typeGestion = typeGestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GestionDTO)) {
            return false;
        }

        GestionDTO gestionDTO = (GestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionDTO{" +
            "id=" + getId() +
            ", typeGestion='" + getTypeGestion() + "'" +
            "}";
    }
}
