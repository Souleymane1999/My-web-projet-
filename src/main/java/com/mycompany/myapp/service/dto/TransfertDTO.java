package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Transfert} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertDTO implements Serializable {

    private Long id;

    private Instant dateTransfert;

    private ImmobilisationDTO immobilisation;

    private AgentDTO agent;

    private StructureDTO structure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTransfert() {
        return dateTransfert;
    }

    public void setDateTransfert(Instant dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    public ImmobilisationDTO getImmobilisation() {
        return immobilisation;
    }

    public void setImmobilisation(ImmobilisationDTO immobilisation) {
        this.immobilisation = immobilisation;
    }

    public AgentDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }

    public StructureDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureDTO structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfertDTO)) {
            return false;
        }

        TransfertDTO transfertDTO = (TransfertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transfertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertDTO{" +
            "id=" + getId() +
            ", dateTransfert='" + getDateTransfert() + "'" +
            ", immobilisation=" + getImmobilisation() +
            ", agent=" + getAgent() +
            ", structure=" + getStructure() +
            "}";
    }
}
