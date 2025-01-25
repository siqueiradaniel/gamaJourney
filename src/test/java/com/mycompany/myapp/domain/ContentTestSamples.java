package com.mycompany.myapp.domain;

import java.util.UUID;

public class ContentTestSamples {

    public static Content getContentSample1() {
        return new Content().id("id1").name("name1").description("description1");
    }

    public static Content getContentSample2() {
        return new Content().id("id2").name("name2").description("description2");
    }

    public static Content getContentRandomSampleGenerator() {
        return new Content().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
