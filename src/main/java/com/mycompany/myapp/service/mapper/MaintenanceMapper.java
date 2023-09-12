package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Immobilisation;
import com.mycompany.myapp.domain.Maintenance;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import com.mycompany.myapp.service.dto.MaintenanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maintenance} and its DTO {@link MaintenanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceMapper extends EntityMapper<MaintenanceDTO, Maintenance> {
    @Mapping(target = "immobilisation", source = "immobilisation", qualifiedByName = "immobilisationId")
    MaintenanceDTO toDto(Maintenance s);

    @Named("immobilisationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImmobilisationDTO toDtoImmobilisationId(Immobilisation immobilisation);
}
