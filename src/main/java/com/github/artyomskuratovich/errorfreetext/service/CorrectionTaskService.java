package com.github.artyomskuratovich.errorfreetext.service;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.dto.TaskCreationResponseDto;

public interface CorrectionTaskService {
    TaskCreationResponseDto enqueue(CorrectionRequestDto requestDto);

    CorrectionResponseDto getDetails(Long id);
}