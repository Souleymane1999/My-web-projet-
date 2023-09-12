package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A AgentStructure.
 */
@Entity
@Table(name = "agent_structure")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "localite" }, allowSetters = true)
    private Structure struture;

    @ManyToOne(fetch = FetchType.LAZY)
    private AgentAffecter agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgentStructure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Structure getStruture() {
        return this.struture;
    }

    public void setStruture(Structure structure) {
        this.struture = structure;
    }

    public AgentStructure struture(Structure structure) {
        this.setStruture(structure);
        return this;
    }

    public AgentAffecter getAgent() {
        return this.agent;
    }

    public void setAgent(AgentAffecter agentAffecter) {
        this.agent = agentAffecter;
    }

    public AgentStructure agent(AgentAffecter agentAffecter) {
        this.setAgent(agentAffecter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentStructure)) {
            return false;
        }
        return id != null && id.equals(((AgentStructure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentStructure{" +
            "id=" + getId() +
            "}";
    }
}
