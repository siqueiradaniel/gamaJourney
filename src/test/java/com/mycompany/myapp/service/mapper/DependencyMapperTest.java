package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.DependencyAsserts.*;
import static com.mycompany.myapp.domain.DependencyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependencyMapperTest {

    private DependencyMapper dependencyMapper;

    @BeforeEach
    void setUp() {
        dependencyMapper = new DependencyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDependencySample1();
        var actual = dependencyMapper.toEntity(dependencyMapper.toDto(expected));
        assertDependencyAllPropertiesEquals(expected, actual);
    }
}
