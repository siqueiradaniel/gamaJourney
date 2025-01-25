package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DependencyTestSamples.*;
import static com.mycompany.myapp.domain.SubcontentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependency.class);
        Dependency dependency1 = getDependencySample1();
        Dependency dependency2 = new Dependency();
        assertThat(dependency1).isNotEqualTo(dependency2);

        dependency2.setId(dependency1.getId());
        assertThat(dependency1).isEqualTo(dependency2);

        dependency2 = getDependencySample2();
        assertThat(dependency1).isNotEqualTo(dependency2);
    }

    @Test
    void subcontentTest() {
        Dependency dependency = getDependencyRandomSampleGenerator();
        Subcontent subcontentBack = getSubcontentRandomSampleGenerator();

        dependency.setSubcontent(subcontentBack);
        assertThat(dependency.getSubcontent()).isEqualTo(subcontentBack);

        dependency.subcontent(null);
        assertThat(dependency.getSubcontent()).isNull();
    }
}
