package com.github.artyomskuratovich.errorfreetext.controller;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CorrectionTaskController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long enqueueTask(@Valid @RequestBody CorrectionRequestDto requestDto) {
        throw new RuntimeException();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CorrectionResponseDto getTaskDetails(@PathVariable Long id) {
        throw new RuntimeException();
    }
}