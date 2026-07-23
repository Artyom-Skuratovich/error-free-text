package com.github.artyomskuratovich.errorfreetext.mapper;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import org.springframework.stereotype.Component;

@Component
public class CorrectionTaskMapper {
    public CorrectionTask toEntity(CorrectionRequestDto dto) {
        return CorrectionTask.builder()
                .text(dto.getText())
                .language(dto.getLanguage())
                .build();
    }

    public CorrectionResponseDto toResponseDto(CorrectionTask entity) {
        var builder = CorrectionResponseDto.builder()
                .status(entity.getStatus());

        switch (entity.getStatus()) {
            case COMPLETED -> builder.text(entity.getText());
            case FAILED -> builder.errorMessage(entity.getErrorMessage());
        }

        return builder.build();
    }
}