package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Exam;
import com.mycompany.myapp.repository.ExamRepository;
import com.mycompany.myapp.service.dto.ExamDTO;
import com.mycompany.myapp.service.mapper.ExamMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Exam}.
 */
@Service
@Transactional
public class ExamService {

    private static final Logger LOG = LoggerFactory.getLogger(ExamService.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    public ExamService(ExamRepository examRepository, ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
    }

    /**
     * Save a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamDTO save(ExamDTO examDTO) {
        LOG.debug("Request to save Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    /**
     * Update a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamDTO update(ExamDTO examDTO) {
        LOG.debug("Request to update Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    /**
     * Partially update a exam.
     *
     * @param examDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamDTO> partialUpdate(ExamDTO examDTO) {
        LOG.debug("Request to partially update Exam : {}", examDTO);

        return examRepository
            .findById(examDTO.getId())
            .map(existingExam -> {
                examMapper.partialUpdate(existingExam, examDTO);

                return existingExam;
            })
            .map(examRepository::save)
            .map(examMapper::toDto);
    }

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Exams");
        return examRepository.findAll(pageable).map(examMapper::toDto);
    }

    /**
     * Get one exam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamDTO> findOne(String id) {
        LOG.debug("Request to get Exam : {}", id);
        return examRepository.findById(id).map(examMapper::toDto);
    }

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }
}
