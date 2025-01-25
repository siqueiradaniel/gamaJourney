package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Exam;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.service.dto.ExamDTO;
import com.mycompany.myapp.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    StudentDTO toDto(Student s);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);
}
