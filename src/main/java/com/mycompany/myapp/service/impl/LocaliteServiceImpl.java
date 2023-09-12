package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Localite;
import com.mycompany.myapp.repository.LocaliteRepository;
import com.mycompany.myapp.service.LocaliteService;
import com.mycompany.myapp.service.dto.LocaliteDTO;
import com.mycompany.myapp.service.mapper.LocaliteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Localite}.
 */
@Service
@Transactional
public class LocaliteServiceImpl implements LocaliteService {

    private final Logger log = LoggerFactory.getLogger(LocaliteServiceImpl.class);

    private final LocaliteRepository localiteRepository;

    private final LocaliteMapper localiteMapper;

    public LocaliteServiceImpl(LocaliteRepository localiteRepository, LocaliteMapper localiteMapper) {
        this.localiteRepository = localiteRepository;
        this.localiteMapper = localiteMapper;
    }

    @Override
    public LocaliteDTO save(LocaliteDTO localiteDTO) {
        log.debug("Request to save Localite : {}", localiteDTO);
        Localite localite = localiteMapper.toEntity(localiteDTO);
        localite = localiteRepository.save(localite);
        return localiteMapper.toDto(localite);
    }

    @Override
    public LocaliteDTO update(LocaliteDTO localiteDTO) {
        log.debug("Request to update Localite : {}", localiteDTO);
        Localite localite = localiteMapper.toEntity(localiteDTO);
        localite = localiteRepository.save(localite);
        return localiteMapper.toDto(localite);
    }

    @Override
    public Optional<LocaliteDTO> partialUpdate(LocaliteDTO localiteDTO) {
        log.debug("Request to partially update Localite : {}", localiteDTO);

        return localiteRepository
            .findById(localiteDTO.getId())
            .map(existingLocalite -> {
                localiteMapper.partialUpdate(existingLocalite, localiteDTO);

                return existingLocalite;
            })
            .map(localiteRepository::save)
            .map(localiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocaliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Localites");
        return localiteRepository.findAll(pageable).map(localiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocaliteDTO> findOne(Long id) {
        log.debug("Request to get Localite : {}", id);
        return localiteRepository.findById(id).map(localiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Localite : {}", id);
        localiteRepository.deleteById(id);
    }
}
