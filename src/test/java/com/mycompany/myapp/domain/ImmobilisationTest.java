package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImmobilisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Immobilisation.class);
        Immobilisation immobilisation1 = new Immobilisation();
        immobilisation1.setId(1L);
        Immobilisation immobilisation2 = new Immobilisation();
        immobilisation2.setId(immobilisation1.getId());
        assertThat(immobilisation1).isEqualTo(immobilisation2);
        immobilisation2.setId(2L);
        assertThat(immobilisation1).isNotEqualTo(immobilisation2);
        immobilisation1.setId(null);
        assertThat(immobilisation1).isNotEqualTo(immobilisation2);
    }
}
