package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.PendingStudentSubcontent;
import com.mycompany.myapp.repository.PendingStudentSubcontentRepository;
import com.mycompany.myapp.service.PendingStudentSubcontentService;
import com.mycompany.myapp.service.dto.PendingStudentSubcontentDTO;
import com.mycompany.myapp.service.mapper.PendingStudentSubcontentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.PendingStudentSubcontent}.
 */
@Service
@Transactional
public class PendingStudentSubcontentServiceImpl implements PendingStudentSubcontentService {

    private static final Logger LOG = LoggerFactory.getLogger(PendingStudentSubcontentServiceImpl.class);

    private final PendingStudentSubcontentRepository pendingStudentSubcontentRepository;

    private final PendingStudentSubcontentMapper pendingStudentSubcontentMapper;

    public PendingStudentSubcontentServiceImpl(
        PendingStudentSubcontentRepository pendingStudentSubcontentRepository,
        PendingStudentSubcontentMapper pendingStudentSubcontentMapper
    ) {
        this.pendingStudentSubcontentRepository = pendingStudentSubcontentRepository;
        this.pendingStudentSubcontentMapper = pendingStudentSubcontentMapper;
    }

    @Override
    public PendingStudentSubcontentDTO save(PendingStudentSubcontentDTO pendingStudentSubcontentDTO) {
        LOG.debug("Request to save PendingStudentSubcontent : {}", pendingStudentSubcontentDTO);
        PendingStudentSubcontent pendingStudentSubcontent = pendingStudentSubcontentMapper.toEntity(pendingStudentSubcontentDTO);
        pendingStudentSubcontent = pendingStudentSubcontentRepository.save(pendingStudentSubcontent);
        return pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);
    }

    @Override
    public PendingStudentSubcontentDTO update(PendingStudentSubcontentDTO pendingStudentSubcontentDTO) {
        LOG.debug("Request to update PendingStudentSubcontent : {}", pendingStudentSubcontentDTO);
        PendingStudentSubcontent pendingStudentSubcontent = pendingStudentSubcontentMapper.toEntity(pendingStudentSubcontentDTO);
        pendingStudentSubcontent = pendingStudentSubcontentRepository.save(pendingStudentSubcontent);
        return pendingStudentSubcontentMapper.toDto(pendingStudentSubcontent);
    }

    @Override
    public Optional<PendingStudentSubcontentDTO> partialUpdate(PendingStudentSubcontentDTO pendingStudentSubcontentDTO) {
        LOG.debug("Request to partially update PendingStudentSubcontent : {}", pendingStudentSubcontentDTO);

        return pendingStudentSubcontentRepository
            .findById(pendingStudentSubcontentDTO.getId())
            .map(existingPendingStudentSubcontent -> {
                pendingStudentSubcontentMapper.partialUpdate(existingPendingStudentSubcontent, pendingStudentSubcontentDTO);

                return existingPendingStudentSubcontent;
            })
            .map(pendingStudentSubcontentRepository::save)
            .map(pendingStudentSubcontentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PendingStudentSubcontentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PendingStudentSubcontents");
        return pendingStudentSubcontentRepository.findAll(pageable).map(pendingStudentSubcontentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PendingStudentSubcontentDTO> findOne(Long id) {
        LOG.debug("Request to get PendingStudentSubcontent : {}", id);
        return pendingStudentSubcontentRepository.findById(id).map(pendingStudentSubcontentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PendingStudentSubcontent : {}", id);
        pendingStudentSubcontentRepository.deleteById(id);
    }
}
