package com.mycompany.myapp.domain;

import java.util.UUID;

public class StudentTestSamples {

    public static Student getStudentSample1() {
        return new Student().id("id1").name("name1");
    }

    public static Student getStudentSample2() {
        return new Student().id("id2").name("name2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
