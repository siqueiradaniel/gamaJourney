package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ExamAsserts.*;
import static com.mycompany.myapp.domain.ExamTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExamMapperTest {

    private ExamMapper examMapper;

    @BeforeEach
    void setUp() {
        examMapper = new ExamMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExamSample1();
        var actual = examMapper.toEntity(examMapper.toDto(expected));
        assertExamAllPropertiesEquals(expected, actual);
    }
}
