package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gestion.class);
        Gestion gestion1 = new Gestion();
        gestion1.setId(1L);
        Gestion gestion2 = new Gestion();
        gestion2.setId(gestion1.getId());
        assertThat(gestion1).isEqualTo(gestion2);
        gestion2.setId(2L);
        assertThat(gestion1).isNotEqualTo(gestion2);
        gestion1.setId(null);
        assertThat(gestion1).isNotEqualTo(gestion2);
    }
}
