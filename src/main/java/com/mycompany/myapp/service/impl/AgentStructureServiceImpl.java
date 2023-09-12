package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AgentStructure;
import com.mycompany.myapp.repository.AgentStructureRepository;
import com.mycompany.myapp.service.AgentStructureService;
import com.mycompany.myapp.service.dto.AgentStructureDTO;
import com.mycompany.myapp.service.mapper.AgentStructureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgentStructure}.
 */
@Service
@Transactional
public class AgentStructureServiceImpl implements AgentStructureService {

    private final Logger log = LoggerFactory.getLogger(AgentStructureServiceImpl.class);

    private final AgentStructureRepository agentStructureRepository;

    private final AgentStructureMapper agentStructureMapper;

    public AgentStructureServiceImpl(AgentStructureRepository agentStructureRepository, AgentStructureMapper agentStructureMapper) {
        this.agentStructureRepository = agentStructureRepository;
        this.agentStructureMapper = agentStructureMapper;
    }

    @Override
    public AgentStructureDTO save(AgentStructureDTO agentStructureDTO) {
        log.debug("Request to save AgentStructure : {}", agentStructureDTO);
        AgentStructure agentStructure = agentStructureMapper.toEntity(agentStructureDTO);
        agentStructure = agentStructureRepository.save(agentStructure);
        return agentStructureMapper.toDto(agentStructure);
    }

    @Override
    public AgentStructureDTO update(AgentStructureDTO agentStructureDTO) {
        log.debug("Request to update AgentStructure : {}", agentStructureDTO);
        AgentStructure agentStructure = agentStructureMapper.toEntity(agentStructureDTO);
        agentStructure = agentStructureRepository.save(agentStructure);
        return agentStructureMapper.toDto(agentStructure);
    }

    @Override
    public Optional<AgentStructureDTO> partialUpdate(AgentStructureDTO agentStructureDTO) {
        log.debug("Request to partially update AgentStructure : {}", agentStructureDTO);

        return agentStructureRepository
            .findById(agentStructureDTO.getId())
            .map(existingAgentStructure -> {
                agentStructureMapper.partialUpdate(existingAgentStructure, agentStructureDTO);

                return existingAgentStructure;
            })
            .map(agentStructureRepository::save)
            .map(agentStructureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentStructureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgentStructures");
        return agentStructureRepository.findAll(pageable).map(agentStructureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgentStructureDTO> findOne(Long id) {
        log.debug("Request to get AgentStructure : {}", id);
        return agentStructureRepository.findById(id).map(agentStructureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgentStructure : {}", id);
        agentStructureRepository.deleteById(id);
    }
}
