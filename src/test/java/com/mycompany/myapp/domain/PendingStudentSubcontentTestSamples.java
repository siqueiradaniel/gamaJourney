package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PendingStudentSubcontentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PendingStudentSubcontent getPendingStudentSubcontentSample1() {
        return new PendingStudentSubcontent().id(1L).studentId("studentId1").subcontentId("subcontentId1").currentStatus("currentStatus1");
    }

    public static PendingStudentSubcontent getPendingStudentSubcontentSample2() {
        return new PendingStudentSubcontent().id(2L).studentId("studentId2").subcontentId("subcontentId2").currentStatus("currentStatus2");
    }

    public static PendingStudentSubcontent getPendingStudentSubcontentRandomSampleGenerator() {
        return new PendingStudentSubcontent()
            .id(longCount.incrementAndGet())
            .studentId(UUID.randomUUID().toString())
            .subcontentId(UUID.randomUUID().toString())
            .currentStatus(UUID.randomUUID().toString());
    }
}
