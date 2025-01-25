package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Dependency;
import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.service.dto.DependencyDTO;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dependency} and its DTO {@link DependencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface DependencyMapper extends EntityMapper<DependencyDTO, Dependency> {
    @Mapping(target = "subcontent", source = "subcontent", qualifiedByName = "subcontentId")
    DependencyDTO toDto(Dependency s);

    @Named("subcontentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubcontentDTO toDtoSubcontentId(Subcontent subcontent);
}
