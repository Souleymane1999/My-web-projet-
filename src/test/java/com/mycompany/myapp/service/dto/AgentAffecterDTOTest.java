package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentAffecterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentAffecterDTO.class);
        AgentAffecterDTO agentAffecterDTO1 = new AgentAffecterDTO();
        agentAffecterDTO1.setId(1L);
        AgentAffecterDTO agentAffecterDTO2 = new AgentAffecterDTO();
        assertThat(agentAffecterDTO1).isNotEqualTo(agentAffecterDTO2);
        agentAffecterDTO2.setId(agentAffecterDTO1.getId());
        assertThat(agentAffecterDTO1).isEqualTo(agentAffecterDTO2);
        agentAffecterDTO2.setId(2L);
        assertThat(agentAffecterDTO1).isNotEqualTo(agentAffecterDTO2);
        agentAffecterDTO1.setId(null);
        assertThat(agentAffecterDTO1).isNotEqualTo(agentAffecterDTO2);
    }
}
