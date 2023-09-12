package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocaliteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocaliteDTO.class);
        LocaliteDTO localiteDTO1 = new LocaliteDTO();
        localiteDTO1.setId(1L);
        LocaliteDTO localiteDTO2 = new LocaliteDTO();
        assertThat(localiteDTO1).isNotEqualTo(localiteDTO2);
        localiteDTO2.setId(localiteDTO1.getId());
        assertThat(localiteDTO1).isEqualTo(localiteDTO2);
        localiteDTO2.setId(2L);
        assertThat(localiteDTO1).isNotEqualTo(localiteDTO2);
        localiteDTO1.setId(null);
        assertThat(localiteDTO1).isNotEqualTo(localiteDTO2);
    }
}
