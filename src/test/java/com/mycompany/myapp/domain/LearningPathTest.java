package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExamTestSamples.*;
import static com.mycompany.myapp.domain.LearningPathTestSamples.*;
import static com.mycompany.myapp.domain.SubcontentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LearningPathTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LearningPath.class);
        LearningPath learningPath1 = getLearningPathSample1();
        LearningPath learningPath2 = new LearningPath();
        assertThat(learningPath1).isNotEqualTo(learningPath2);

        learningPath2.setId(learningPath1.getId());
        assertThat(learningPath1).isEqualTo(learningPath2);

        learningPath2 = getLearningPathSample2();
        assertThat(learningPath1).isNotEqualTo(learningPath2);
    }

    @Test
    void examTest() {
        LearningPath learningPath = getLearningPathRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        learningPath.setExam(examBack);
        assertThat(learningPath.getExam()).isEqualTo(examBack);

        learningPath.exam(null);
        assertThat(learningPath.getExam()).isNull();
    }

    @Test
    void subcontentTest() {
        LearningPath learningPath = getLearningPathRandomSampleGenerator();
        Subcontent subcontentBack = getSubcontentRandomSampleGenerator();

        learningPath.setSubcontent(subcontentBack);
        assertThat(learningPath.getSubcontent()).isEqualTo(subcontentBack);

        learningPath.subcontent(null);
        assertThat(learningPath.getSubcontent()).isNull();
    }
}
