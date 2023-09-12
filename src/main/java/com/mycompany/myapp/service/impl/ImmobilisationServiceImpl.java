package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Immobilisation;
import com.mycompany.myapp.repository.ImmobilisationRepository;
import com.mycompany.myapp.service.ImmobilisationService;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import com.mycompany.myapp.service.mapper.ImmobilisationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Immobilisation}.
 */
@Service
@Transactional
public class ImmobilisationServiceImpl implements ImmobilisationService {

    private final Logger log = LoggerFactory.getLogger(ImmobilisationServiceImpl.class);

    private final ImmobilisationRepository immobilisationRepository;

    private final ImmobilisationMapper immobilisationMapper;

    public ImmobilisationServiceImpl(ImmobilisationRepository immobilisationRepository, ImmobilisationMapper immobilisationMapper) {
        this.immobilisationRepository = immobilisationRepository;
        this.immobilisationMapper = immobilisationMapper;
    }

    @Override
    public ImmobilisationDTO save(ImmobilisationDTO immobilisationDTO) {
        log.debug("Request to save Immobilisation : {}", immobilisationDTO);
        Immobilisation immobilisation = immobilisationMapper.toEntity(immobilisationDTO);
        immobilisation = immobilisationRepository.save(immobilisation);
        return immobilisationMapper.toDto(immobilisation);
    }

    @Override
    public ImmobilisationDTO update(ImmobilisationDTO immobilisationDTO) {
        log.debug("Request to update Immobilisation : {}", immobilisationDTO);
        Immobilisation immobilisation = immobilisationMapper.toEntity(immobilisationDTO);
        immobilisation = immobilisationRepository.save(immobilisation);
        return immobilisationMapper.toDto(immobilisation);
    }

    @Override
    public Optional<ImmobilisationDTO> partialUpdate(ImmobilisationDTO immobilisationDTO) {
        log.debug("Request to partially update Immobilisation : {}", immobilisationDTO);

        return immobilisationRepository
            .findById(immobilisationDTO.getId())
            .map(existingImmobilisation -> {
                immobilisationMapper.partialUpdate(existingImmobilisation, immobilisationDTO);

                return existingImmobilisation;
            })
            .map(immobilisationRepository::save)
            .map(immobilisationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImmobilisationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Immobilisations");
        return immobilisationRepository.findAll(pageable).map(immobilisationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImmobilisationDTO> findOne(Long id) {
        log.debug("Request to get Immobilisation : {}", id);
        return immobilisationRepository.findById(id).map(immobilisationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Immobilisation : {}", id);
        immobilisationRepository.deleteById(id);
    }
}
