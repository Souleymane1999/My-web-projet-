package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AgentAffecter;
import com.mycompany.myapp.domain.AgentStructure;
import com.mycompany.myapp.domain.Structure;
import com.mycompany.myapp.service.dto.AgentAffecterDTO;
import com.mycompany.myapp.service.dto.AgentStructureDTO;
import com.mycompany.myapp.service.dto.StructureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentStructure} and its DTO {@link AgentStructureDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentStructureMapper extends EntityMapper<AgentStructureDTO, AgentStructure> {
    @Mapping(target = "struture", source = "struture", qualifiedByName = "structureId")
    @Mapping(target = "agent", source = "agent", qualifiedByName = "agentAffecterId")
    AgentStructureDTO toDto(AgentStructure s);

    @Named("structureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StructureDTO toDtoStructureId(Structure structure);

    @Named("agentAffecterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentAffecterDTO toDtoAgentAffecterId(AgentAffecter agentAffecter);
}
