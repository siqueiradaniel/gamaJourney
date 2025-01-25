package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.PendingStudentSubcontent;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.service.dto.PendingStudentSubcontentDTO;
import com.mycompany.myapp.service.dto.StudentDTO;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PendingStudentSubcontent} and its DTO {@link PendingStudentSubcontentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PendingStudentSubcontentMapper extends EntityMapper<PendingStudentSubcontentDTO, PendingStudentSubcontent> {
    @Mapping(target = "subcontent", source = "subcontent", qualifiedByName = "subcontentId")
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    PendingStudentSubcontentDTO toDto(PendingStudentSubcontent s);

    @Named("subcontentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubcontentDTO toDtoSubcontentId(Subcontent subcontent);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentDTO toDtoStudentId(Student student);
}
