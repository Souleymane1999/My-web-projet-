package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AgentAffecter;
import com.mycompany.myapp.repository.AgentAffecterRepository;
import com.mycompany.myapp.service.AgentAffecterService;
import com.mycompany.myapp.service.dto.AgentAffecterDTO;
import com.mycompany.myapp.service.mapper.AgentAffecterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgentAffecter}.
 */
@Service
@Transactional
public class AgentAffecterServiceImpl implements AgentAffecterService {

    private final Logger log = LoggerFactory.getLogger(AgentAffecterServiceImpl.class);

    private final AgentAffecterRepository agentAffecterRepository;

    private final AgentAffecterMapper agentAffecterMapper;

    public AgentAffecterServiceImpl(AgentAffecterRepository agentAffecterRepository, AgentAffecterMapper agentAffecterMapper) {
        this.agentAffecterRepository = agentAffecterRepository;
        this.agentAffecterMapper = agentAffecterMapper;
    }

    @Override
    public AgentAffecterDTO save(AgentAffecterDTO agentAffecterDTO) {
        log.debug("Request to save AgentAffecter : {}", agentAffecterDTO);
        AgentAffecter agentAffecter = agentAffecterMapper.toEntity(agentAffecterDTO);
        agentAffecter = agentAffecterRepository.save(agentAffecter);
        return agentAffecterMapper.toDto(agentAffecter);
    }

    @Override
    public AgentAffecterDTO update(AgentAffecterDTO agentAffecterDTO) {
        log.debug("Request to update AgentAffecter : {}", agentAffecterDTO);
        AgentAffecter agentAffecter = agentAffecterMapper.toEntity(agentAffecterDTO);
        agentAffecter = agentAffecterRepository.save(agentAffecter);
        return agentAffecterMapper.toDto(agentAffecter);
    }

    @Override
    public Optional<AgentAffecterDTO> partialUpdate(AgentAffecterDTO agentAffecterDTO) {
        log.debug("Request to partially update AgentAffecter : {}", agentAffecterDTO);

        return agentAffecterRepository
            .findById(agentAffecterDTO.getId())
            .map(existingAgentAffecter -> {
                agentAffecterMapper.partialUpdate(existingAgentAffecter, agentAffecterDTO);

                return existingAgentAffecter;
            })
            .map(agentAffecterRepository::save)
            .map(agentAffecterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentAffecterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgentAffecters");
        return agentAffecterRepository.findAll(pageable).map(agentAffecterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgentAffecterDTO> findOne(Long id) {
        log.debug("Request to get AgentAffecter : {}", id);
        return agentAffecterRepository.findById(id).map(agentAffecterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgentAffecter : {}", id);
        agentAffecterRepository.deleteById(id);
    }
}
