package com.github.artyomskuratovich.errorfreetext.service.impl;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.dto.TaskCreationResponseDto;
import com.github.artyomskuratovich.errorfreetext.exception.EntityNotFoundException;
import com.github.artyomskuratovich.errorfreetext.mapper.CorrectionTaskMapper;
import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import com.github.artyomskuratovich.errorfreetext.repository.CorrectionTaskRepository;
import com.github.artyomskuratovich.errorfreetext.service.CorrectionTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CorrectionTaskServiceImpl implements CorrectionTaskService {
    private final CorrectionTaskRepository repository;
    private final CorrectionTaskMapper mapper;

    @Override
    public TaskCreationResponseDto enqueue(CorrectionRequestDto requestDto) {
        CorrectionTask task = mapper.toEntity(requestDto);
        task.setStatus(TaskStatus.CREATED);

        CorrectionTask created = repository.save(task);
        log.debug("Task saved to database. ID: {}, Status: {}", created.getId(), created.getStatus());

        return new TaskCreationResponseDto(created.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public CorrectionResponseDto getDetails(UUID id) {
        return repository.findById(id)
                .map(task -> {
                    log.debug("Task found for ID: {}. Current status: {}", id, task.getStatus());
                    return mapper.toResponseDto(task);
                })
                .orElseThrow(() -> {
                    log.warn("Fetch failed: task with ID: {} not found in the database", id);
                    return new EntityNotFoundException("Task with ID: " + id + " not found");
                });
    }
}
