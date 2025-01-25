package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContentTestSamples.*;
import static com.mycompany.myapp.domain.DependencyTestSamples.*;
import static com.mycompany.myapp.domain.LearningPathTestSamples.*;
import static com.mycompany.myapp.domain.PendingStudentSubcontentTestSamples.*;
import static com.mycompany.myapp.domain.SubcontentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubcontentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subcontent.class);
        Subcontent subcontent1 = getSubcontentSample1();
        Subcontent subcontent2 = new Subcontent();
        assertThat(subcontent1).isNotEqualTo(subcontent2);

        subcontent2.setId(subcontent1.getId());
        assertThat(subcontent1).isEqualTo(subcontent2);

        subcontent2 = getSubcontentSample2();
        assertThat(subcontent1).isNotEqualTo(subcontent2);
    }

    @Test
    void learningPathTest() {
        Subcontent subcontent = getSubcontentRandomSampleGenerator();
        LearningPath learningPathBack = getLearningPathRandomSampleGenerator();

        subcontent.addLearningPath(learningPathBack);
        assertThat(subcontent.getLearningPaths()).containsOnly(learningPathBack);
        assertThat(learningPathBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.removeLearningPath(learningPathBack);
        assertThat(subcontent.getLearningPaths()).doesNotContain(learningPathBack);
        assertThat(learningPathBack.getSubcontent()).isNull();

        subcontent.learningPaths(new HashSet<>(Set.of(learningPathBack)));
        assertThat(subcontent.getLearningPaths()).containsOnly(learningPathBack);
        assertThat(learningPathBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.setLearningPaths(new HashSet<>());
        assertThat(subcontent.getLearningPaths()).doesNotContain(learningPathBack);
        assertThat(learningPathBack.getSubcontent()).isNull();
    }

    @Test
    void dependencyTest() {
        Subcontent subcontent = getSubcontentRandomSampleGenerator();
        Dependency dependencyBack = getDependencyRandomSampleGenerator();

        subcontent.addDependency(dependencyBack);
        assertThat(subcontent.getDependencies()).containsOnly(dependencyBack);
        assertThat(dependencyBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.removeDependency(dependencyBack);
        assertThat(subcontent.getDependencies()).doesNotContain(dependencyBack);
        assertThat(dependencyBack.getSubcontent()).isNull();

        subcontent.dependencies(new HashSet<>(Set.of(dependencyBack)));
        assertThat(subcontent.getDependencies()).containsOnly(dependencyBack);
        assertThat(dependencyBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.setDependencies(new HashSet<>());
        assertThat(subcontent.getDependencies()).doesNotContain(dependencyBack);
        assertThat(dependencyBack.getSubcontent()).isNull();
    }

    @Test
    void pendingStudentSubcontentTest() {
        Subcontent subcontent = getSubcontentRandomSampleGenerator();
        PendingStudentSubcontent pendingStudentSubcontentBack = getPendingStudentSubcontentRandomSampleGenerator();

        subcontent.addPendingStudentSubcontent(pendingStudentSubcontentBack);
        assertThat(subcontent.getPendingStudentSubcontents()).containsOnly(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.removePendingStudentSubcontent(pendingStudentSubcontentBack);
        assertThat(subcontent.getPendingStudentSubcontents()).doesNotContain(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getSubcontent()).isNull();

        subcontent.pendingStudentSubcontents(new HashSet<>(Set.of(pendingStudentSubcontentBack)));
        assertThat(subcontent.getPendingStudentSubcontents()).containsOnly(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getSubcontent()).isEqualTo(subcontent);

        subcontent.setPendingStudentSubcontents(new HashSet<>());
        assertThat(subcontent.getPendingStudentSubcontents()).doesNotContain(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getSubcontent()).isNull();
    }

    @Test
    void contentTest() {
        Subcontent subcontent = getSubcontentRandomSampleGenerator();
        Content contentBack = getContentRandomSampleGenerator();

        subcontent.setContent(contentBack);
        assertThat(subcontent.getContent()).isEqualTo(contentBack);

        subcontent.content(null);
        assertThat(subcontent.getContent()).isNull();
    }
}
