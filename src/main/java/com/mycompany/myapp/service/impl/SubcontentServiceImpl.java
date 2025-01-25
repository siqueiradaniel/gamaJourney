package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Subcontent;
import com.mycompany.myapp.repository.SubcontentRepository;
import com.mycompany.myapp.service.SubcontentService;
import com.mycompany.myapp.service.dto.SubcontentDTO;
import com.mycompany.myapp.service.mapper.SubcontentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Subcontent}.
 */
@Service
@Transactional
public class SubcontentServiceImpl implements SubcontentService {

    private static final Logger LOG = LoggerFactory.getLogger(SubcontentServiceImpl.class);

    private final SubcontentRepository subcontentRepository;

    private final SubcontentMapper subcontentMapper;

    public SubcontentServiceImpl(SubcontentRepository subcontentRepository, SubcontentMapper subcontentMapper) {
        this.subcontentRepository = subcontentRepository;
        this.subcontentMapper = subcontentMapper;
    }

    @Override
    public SubcontentDTO save(SubcontentDTO subcontentDTO) {
        LOG.debug("Request to save Subcontent : {}", subcontentDTO);
        Subcontent subcontent = subcontentMapper.toEntity(subcontentDTO);
        subcontent = subcontentRepository.save(subcontent);
        return subcontentMapper.toDto(subcontent);
    }

    @Override
    public SubcontentDTO update(SubcontentDTO subcontentDTO) {
        LOG.debug("Request to update Subcontent : {}", subcontentDTO);
        Subcontent subcontent = subcontentMapper.toEntity(subcontentDTO);
        subcontent = subcontentRepository.save(subcontent);
        return subcontentMapper.toDto(subcontent);
    }

    @Override
    public Optional<SubcontentDTO> partialUpdate(SubcontentDTO subcontentDTO) {
        LOG.debug("Request to partially update Subcontent : {}", subcontentDTO);

        return subcontentRepository
            .findById(subcontentDTO.getId())
            .map(existingSubcontent -> {
                subcontentMapper.partialUpdate(existingSubcontent, subcontentDTO);

                return existingSubcontent;
            })
            .map(subcontentRepository::save)
            .map(subcontentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubcontentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Subcontents");
        return subcontentRepository.findAll(pageable).map(subcontentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubcontentDTO> findOne(String id) {
        LOG.debug("Request to get Subcontent : {}", id);
        return subcontentRepository.findById(id).map(subcontentMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Subcontent : {}", id);
        subcontentRepository.deleteById(id);
    }
}
