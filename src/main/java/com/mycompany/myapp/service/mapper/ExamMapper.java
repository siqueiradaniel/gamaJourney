package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Exam;
import com.mycompany.myapp.service.dto.ExamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {}
