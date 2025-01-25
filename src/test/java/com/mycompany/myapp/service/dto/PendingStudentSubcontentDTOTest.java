package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PendingStudentSubcontentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PendingStudentSubcontentDTO.class);
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO1 = new PendingStudentSubcontentDTO();
        pendingStudentSubcontentDTO1.setId(1L);
        PendingStudentSubcontentDTO pendingStudentSubcontentDTO2 = new PendingStudentSubcontentDTO();
        assertThat(pendingStudentSubcontentDTO1).isNotEqualTo(pendingStudentSubcontentDTO2);
        pendingStudentSubcontentDTO2.setId(pendingStudentSubcontentDTO1.getId());
        assertThat(pendingStudentSubcontentDTO1).isEqualTo(pendingStudentSubcontentDTO2);
        pendingStudentSubcontentDTO2.setId(2L);
        assertThat(pendingStudentSubcontentDTO1).isNotEqualTo(pendingStudentSubcontentDTO2);
        pendingStudentSubcontentDTO1.setId(null);
        assertThat(pendingStudentSubcontentDTO1).isNotEqualTo(pendingStudentSubcontentDTO2);
    }
}
