package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentStructureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentStructureDTO.class);
        AgentStructureDTO agentStructureDTO1 = new AgentStructureDTO();
        agentStructureDTO1.setId(1L);
        AgentStructureDTO agentStructureDTO2 = new AgentStructureDTO();
        assertThat(agentStructureDTO1).isNotEqualTo(agentStructureDTO2);
        agentStructureDTO2.setId(agentStructureDTO1.getId());
        assertThat(agentStructureDTO1).isEqualTo(agentStructureDTO2);
        agentStructureDTO2.setId(2L);
        assertThat(agentStructureDTO1).isNotEqualTo(agentStructureDTO2);
        agentStructureDTO1.setId(null);
        assertThat(agentStructureDTO1).isNotEqualTo(agentStructureDTO2);
    }
}
