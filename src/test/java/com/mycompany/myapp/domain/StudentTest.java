package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExamTestSamples.*;
import static com.mycompany.myapp.domain.PendingStudentSubcontentTestSamples.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void pendingStudentSubcontentTest() {
        Student student = getStudentRandomSampleGenerator();
        PendingStudentSubcontent pendingStudentSubcontentBack = getPendingStudentSubcontentRandomSampleGenerator();

        student.addPendingStudentSubcontent(pendingStudentSubcontentBack);
        assertThat(student.getPendingStudentSubcontents()).containsOnly(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getStudent()).isEqualTo(student);

        student.removePendingStudentSubcontent(pendingStudentSubcontentBack);
        assertThat(student.getPendingStudentSubcontents()).doesNotContain(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getStudent()).isNull();

        student.pendingStudentSubcontents(new HashSet<>(Set.of(pendingStudentSubcontentBack)));
        assertThat(student.getPendingStudentSubcontents()).containsOnly(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getStudent()).isEqualTo(student);

        student.setPendingStudentSubcontents(new HashSet<>());
        assertThat(student.getPendingStudentSubcontents()).doesNotContain(pendingStudentSubcontentBack);
        assertThat(pendingStudentSubcontentBack.getStudent()).isNull();
    }

    @Test
    void examTest() {
        Student student = getStudentRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        student.setExam(examBack);
        assertThat(student.getExam()).isEqualTo(examBack);

        student.exam(null);
        assertThat(student.getExam()).isNull();
    }
}
