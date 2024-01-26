package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Agent;
import com.mycompany.myapp.domain.Immobilisation;
import com.mycompany.myapp.domain.Structure;
import com.mycompany.myapp.domain.Transfert;
import com.mycompany.myapp.service.dto.AgentDTO;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import com.mycompany.myapp.service.dto.StructureDTO;
import com.mycompany.myapp.service.dto.TransfertDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transfert} and its DTO {@link TransfertDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransfertMapper extends EntityMapper<TransfertDTO, Transfert> {
    @Mapping(target = "immobilisation", source = "immobilisation", qualifiedByName = "immobilisationId")
    @Mapping(target = "agent", source = "agent", qualifiedByName = "agentId")
    @Mapping(target = "structure", source = "structure", qualifiedByName = "structureId")
    TransfertDTO toDto(Transfert s);

    @Named("immobilisationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImmobilisationDTO toDtoImmobilisationId(Immobilisation immobilisation);

    @Named("agentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentDTO toDtoAgentId(Agent agent);

    @Named("structureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StructureDTO toDtoStructureId(Structure structure);
}
