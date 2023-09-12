package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AgentStructure} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentStructureDTO implements Serializable {

    private Long id;

    private StructureDTO struture;

    private AgentAffecterDTO agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StructureDTO getStruture() {
        return struture;
    }

    public void setStruture(StructureDTO struture) {
        this.struture = struture;
    }

    public AgentAffecterDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentAffecterDTO agent) {
        this.agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentStructureDTO)) {
            return false;
        }

        AgentStructureDTO agentStructureDTO = (AgentStructureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentStructureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentStructureDTO{" +
            "id=" + getId() +
            ", struture=" + getStruture() +
            ", agent=" + getAgent() +
            "}";
    }
}
