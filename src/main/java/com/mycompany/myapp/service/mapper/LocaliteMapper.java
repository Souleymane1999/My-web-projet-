package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.service.dto.LocaliteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Localite} and its DTO {@link LocaliteDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocaliteMapper extends EntityMapper<LocaliteDTO, Localite> {}
