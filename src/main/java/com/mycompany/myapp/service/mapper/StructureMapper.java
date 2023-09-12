package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.domain.Structure;
import com.mycompany.myapp.service.dto.LocaliteDTO;
import com.mycompany.myapp.service.dto.StructureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Structure} and its DTO {@link StructureDTO}.
 */
@Mapper(componentModel = "spring")
public interface StructureMapper extends EntityMapper<StructureDTO, Structure> {
    @Mapping(target = "localite", source = "localite", qualifiedByName = "localiteId")
    StructureDTO toDto(Structure s);

    @Named("localiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocaliteDTO toDtoLocaliteId(Localite localite);
}
