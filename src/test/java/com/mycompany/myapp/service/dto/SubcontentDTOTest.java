package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubcontentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubcontentDTO.class);
        SubcontentDTO subcontentDTO1 = new SubcontentDTO();
        subcontentDTO1.setId("id1");
        SubcontentDTO subcontentDTO2 = new SubcontentDTO();
        assertThat(subcontentDTO1).isNotEqualTo(subcontentDTO2);
        subcontentDTO2.setId(subcontentDTO1.getId());
        assertThat(subcontentDTO1).isEqualTo(subcontentDTO2);
        subcontentDTO2.setId("id2");
        assertThat(subcontentDTO1).isNotEqualTo(subcontentDTO2);
        subcontentDTO1.setId(null);
        assertThat(subcontentDTO1).isNotEqualTo(subcontentDTO2);
    }
}
