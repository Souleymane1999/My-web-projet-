package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Transfert;
import com.mycompany.myapp.repository.TransfertRepository;
import com.mycompany.myapp.service.TransfertService;
import com.mycompany.myapp.service.dto.TransfertDTO;
import com.mycompany.myapp.service.mapper.TransfertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transfert}.
 */
@Service
@Transactional
public class TransfertServiceImpl implements TransfertService {

    private final Logger log = LoggerFactory.getLogger(TransfertServiceImpl.class);

    private final TransfertRepository transfertRepository;

    private final TransfertMapper transfertMapper;

    public TransfertServiceImpl(TransfertRepository transfertRepository, TransfertMapper transfertMapper) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
    }

    @Override
    public TransfertDTO save(TransfertDTO transfertDTO) {
        log.debug("Request to save Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public TransfertDTO update(TransfertDTO transfertDTO) {
        log.debug("Request to update Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public Optional<TransfertDTO> partialUpdate(TransfertDTO transfertDTO) {
        log.debug("Request to partially update Transfert : {}", transfertDTO);

        return transfertRepository
            .findById(transfertDTO.getId())
            .map(existingTransfert -> {
                transfertMapper.partialUpdate(existingTransfert, transfertDTO);

                return existingTransfert;
            })
            .map(transfertRepository::save)
            .map(transfertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransfertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transferts");
        return transfertRepository.findAll(pageable).map(transfertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfertDTO> findOne(Long id) {
        log.debug("Request to get Transfert : {}", id);
        return transfertRepository.findById(id).map(transfertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transfert : {}", id);
        transfertRepository.deleteById(id);
    }
}
