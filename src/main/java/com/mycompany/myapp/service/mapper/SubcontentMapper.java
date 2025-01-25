package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Content;
import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.service.dto.ContentDTO;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subcontent} and its DTO {@link SubcontentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubcontentMapper extends EntityMapper<SubcontentDTO, Subcontent> {
    @Mapping(target = "content", source = "content", qualifiedByName = "contentId")
    SubcontentDTO toDto(Subcontent s);

    @Named("contentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentDTO toDtoContentId(Content content);
}
