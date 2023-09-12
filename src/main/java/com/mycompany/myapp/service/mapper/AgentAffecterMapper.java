package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AgentAffecter;
import com.mycompany.myapp.service.dto.AgentAffecterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentAffecter} and its DTO {@link AgentAffecterDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentAffecterMapper extends EntityMapper<AgentAffecterDTO, AgentAffecter> {}
