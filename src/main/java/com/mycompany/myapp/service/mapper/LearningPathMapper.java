package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Exam;
import com.mycompany.myapp.domain.LearningPath;
import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.service.dto.ExamDTO;
import com.mycompany.myapp.service.dto.LearningPathDTO;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LearningPath} and its DTO {@link LearningPathDTO}.
 */
@Mapper(componentModel = "spring")
public interface LearningPathMapper extends EntityMapper<LearningPathDTO, LearningPath> {
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    @Mapping(target = "subcontent", source = "subcontent", qualifiedByName = "subcontentId")
    LearningPathDTO toDto(LearningPath s);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("subcontentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubcontentDTO toDtoSubcontentId(Subcontent subcontent);
}
