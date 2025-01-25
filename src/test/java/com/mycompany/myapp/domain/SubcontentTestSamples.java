package com.mycompany.myapp.domain;

import java.util.UUID;

public class SubcontentTestSamples {

    public static Subcontent getSubcontentSample1() {
        return new Subcontent().id("id1").name("name1").description("description1");
    }

    public static Subcontent getSubcontentSample2() {
        return new Subcontent().id("id2").name("name2").description("description2");
    }

    public static Subcontent getSubcontentRandomSampleGenerator() {
        return new Subcontent()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
