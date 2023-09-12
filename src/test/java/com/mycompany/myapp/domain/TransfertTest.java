package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transfert.class);
        Transfert transfert1 = new Transfert();
        transfert1.setId(1L);
        Transfert transfert2 = new Transfert();
        transfert2.setId(transfert1.getId());
        assertThat(transfert1).isEqualTo(transfert2);
        transfert2.setId(2L);
        assertThat(transfert1).isNotEqualTo(transfert2);
        transfert1.setId(null);
        assertThat(transfert1).isNotEqualTo(transfert2);
    }
}
