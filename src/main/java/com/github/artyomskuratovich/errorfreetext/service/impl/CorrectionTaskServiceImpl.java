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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CorrectionTaskServiceImpl implements CorrectionTaskService {
    private final CorrectionTaskRepository repository;
    private final CorrectionTaskMapper mapper;

    @Override
    public TaskCreationResponseDto enqueue(CorrectionRequestDto requestDto) {
        CorrectionTask task = mapper.toEntity(requestDto);
        task.setStatus(TaskStatus.CREATED);

        CorrectionTask created = repository.save(task);

        return new TaskCreationResponseDto(created.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public CorrectionResponseDto getDetails(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));
    }
}
