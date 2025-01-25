package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.StudentAsserts.*;
import static com.mycompany.myapp.domain.StudentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentSample1();
        var actual = studentMapper.toEntity(studentMapper.toDto(expected));
        assertStudentAllPropertiesEquals(expected, actual);
    }
}
