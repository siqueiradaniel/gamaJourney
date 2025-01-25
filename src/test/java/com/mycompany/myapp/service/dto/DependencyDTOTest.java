package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependencyDTO.class);
        DependencyDTO dependencyDTO1 = new DependencyDTO();
        dependencyDTO1.setId(1L);
        DependencyDTO dependencyDTO2 = new DependencyDTO();
        assertThat(dependencyDTO1).isNotEqualTo(dependencyDTO2);
        dependencyDTO2.setId(dependencyDTO1.getId());
        assertThat(dependencyDTO1).isEqualTo(dependencyDTO2);
        dependencyDTO2.setId(2L);
        assertThat(dependencyDTO1).isNotEqualTo(dependencyDTO2);
        dependencyDTO1.setId(null);
        assertThat(dependencyDTO1).isNotEqualTo(dependencyDTO2);
    }
}
