package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentAffecterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentAffecter.class);
        AgentAffecter agentAffecter1 = new AgentAffecter();
        agentAffecter1.setId(1L);
        AgentAffecter agentAffecter2 = new AgentAffecter();
        agentAffecter2.setId(agentAffecter1.getId());
        assertThat(agentAffecter1).isEqualTo(agentAffecter2);
        agentAffecter2.setId(2L);
        assertThat(agentAffecter1).isNotEqualTo(agentAffecter2);
        agentAffecter1.setId(null);
        assertThat(agentAffecter1).isNotEqualTo(agentAffecter2);
    }
}
