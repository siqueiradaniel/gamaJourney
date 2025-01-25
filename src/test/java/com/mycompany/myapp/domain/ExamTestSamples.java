package com.mycompany.myapp.domain;

import java.util.UUID;

public class ExamTestSamples {

    public static Exam getExamSample1() {
        return new Exam().id("id1").name("name1").description("description1");
    }

    public static Exam getExamSample2() {
        return new Exam().id("id2").name("name2").description("description2");
    }

    public static Exam getExamRandomSampleGenerator() {
        return new Exam().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
