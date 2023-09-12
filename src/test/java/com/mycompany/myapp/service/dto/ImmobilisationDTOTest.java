package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImmobilisationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImmobilisationDTO.class);
        ImmobilisationDTO immobilisationDTO1 = new ImmobilisationDTO();
        immobilisationDTO1.setId(1L);
        ImmobilisationDTO immobilisationDTO2 = new ImmobilisationDTO();
        assertThat(immobilisationDTO1).isNotEqualTo(immobilisationDTO2);
        immobilisationDTO2.setId(immobilisationDTO1.getId());
        assertThat(immobilisationDTO1).isEqualTo(immobilisationDTO2);
        immobilisationDTO2.setId(2L);
        assertThat(immobilisationDTO1).isNotEqualTo(immobilisationDTO2);
        immobilisationDTO1.setId(null);
        assertThat(immobilisationDTO1).isNotEqualTo(immobilisationDTO2);
    }
}
