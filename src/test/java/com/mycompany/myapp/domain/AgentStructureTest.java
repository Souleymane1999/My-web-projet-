package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentStructureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentStructure.class);
        AgentStructure agentStructure1 = new AgentStructure();
        agentStructure1.setId(1L);
        AgentStructure agentStructure2 = new AgentStructure();
        agentStructure2.setId(agentStructure1.getId());
        assertThat(agentStructure1).isEqualTo(agentStructure2);
        agentStructure2.setId(2L);
        assertThat(agentStructure1).isNotEqualTo(agentStructure2);
        agentStructure1.setId(null);
        assertThat(agentStructure1).isNotEqualTo(agentStructure2);
    }
}
