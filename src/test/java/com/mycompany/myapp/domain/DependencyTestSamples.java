package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DependencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dependency getDependencySample1() {
        return new Dependency().id(1L).firstSubcontentId("firstSubcontentId1").secondSubcontentId("secondSubcontentId1");
    }

    public static Dependency getDependencySample2() {
        return new Dependency().id(2L).firstSubcontentId("firstSubcontentId2").secondSubcontentId("secondSubcontentId2");
    }

    public static Dependency getDependencyRandomSampleGenerator() {
        return new Dependency()
            .id(longCount.incrementAndGet())
            .firstSubcontentId(UUID.randomUUID().toString())
            .secondSubcontentId(UUID.randomUUID().toString());
    }
}
