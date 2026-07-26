package com.github.artyomskuratovich.errorfreetext.controller;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.dto.TaskCreationResponseDto;
import com.github.artyomskuratovich.errorfreetext.service.CorrectionTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class CorrectionTaskController {
    private final CorrectionTaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskCreationResponseDto enqueueTask(@Valid @RequestBody CorrectionRequestDto requestDto) {
        log.info("Received request to create text correction task");
        TaskCreationResponseDto response = service.enqueue(requestDto);
        log.info("Task successfully enqueued. Task ID: {}", response.getTaskId());
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CorrectionResponseDto getTaskDetails(@PathVariable UUID id) {
        log.debug("Received request to get details for task ID: {}", id);
        return service.getDetails(id);
    }
}