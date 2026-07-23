package com.github.artyomskuratovich.errorfreetext.controller;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.dto.TaskCreationResponseDto;
import com.github.artyomskuratovich.errorfreetext.service.CorrectionTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CorrectionTaskController {
    private final CorrectionTaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskCreationResponseDto enqueueTask(@Valid @RequestBody CorrectionRequestDto requestDto) {
        return service.enqueue(requestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CorrectionResponseDto getTaskDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }
}