package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Structure;
import com.mycompany.myapp.repository.StructureRepository;
import com.mycompany.myapp.service.StructureService;
import com.mycompany.myapp.service.dto.StructureDTO;
import com.mycompany.myapp.service.mapper.StructureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Structure}.
 */
@Service
@Transactional
public class StructureServiceImpl implements StructureService {

    private final Logger log = LoggerFactory.getLogger(StructureServiceImpl.class);

    private final StructureRepository structureRepository;

    private final StructureMapper structureMapper;

    public StructureServiceImpl(StructureRepository structureRepository, StructureMapper structureMapper) {
        this.structureRepository = structureRepository;
        this.structureMapper = structureMapper;
    }

    @Override
    public StructureDTO save(StructureDTO structureDTO) {
        log.debug("Request to save Structure : {}", structureDTO);
        Structure structure = structureMapper.toEntity(structureDTO);
        structure = structureRepository.save(structure);
        return structureMapper.toDto(structure);
    }

    @Override
    public StructureDTO update(StructureDTO structureDTO) {
        log.debug("Request to update Structure : {}", structureDTO);
        Structure structure = structureMapper.toEntity(structureDTO);
        structure = structureRepository.save(structure);
        return structureMapper.toDto(structure);
    }

    @Override
    public Optional<StructureDTO> partialUpdate(StructureDTO structureDTO) {
        log.debug("Request to partially update Structure : {}", structureDTO);

        return structureRepository
            .findById(structureDTO.getId())
            .map(existingStructure -> {
                structureMapper.partialUpdate(existingStructure, structureDTO);

                return existingStructure;
            })
            .map(structureRepository::save)
            .map(structureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StructureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Structures");
        return structureRepository.findAll(pageable).map(structureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StructureDTO> findOne(Long id) {
        log.debug("Request to get Structure : {}", id);
        return structureRepository.findById(id).map(structureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Structure : {}", id);
        structureRepository.deleteById(id);
    }
}
