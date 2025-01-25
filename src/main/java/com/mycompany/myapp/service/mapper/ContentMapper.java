package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Content;
import com.mycompany.myapp.service.dto.ContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Content} and its DTO {@link ContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentMapper extends EntityMapper<ContentDTO, Content> {}
