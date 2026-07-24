package com.github.artyomskuratovich.errorfreetext.mapper;

import com.github.artyomskuratovich.errorfreetext.dto.CorrectionRequestDto;
import com.github.artyomskuratovich.errorfreetext.dto.CorrectionResponseDto;
import com.github.artyomskuratovich.errorfreetext.model.CorrectionTask;
import com.github.artyomskuratovich.errorfreetext.model.Language;
import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrectionTaskMapperTest {
    private final CorrectionTaskMapper mapper = new CorrectionTaskMapper();

    @Test
    void toEntity_ValidDto_ReturnsEntity() {
        CorrectionRequestDto dto = new CorrectionRequestDto("Hello world", Language.EN);

        CorrectionTask entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Hello world", entity.getText());
        assertEquals(Language.EN, entity.getLanguage());
        assertNull(entity.getStatus());
    }

    @Test
    void toResponseDto_CompletedTask_ReturnsText() {
        CorrectionTask task = CorrectionTask.builder()
                .status(TaskStatus.COMPLETED)
                .text("Corrected text")
                .build();

        CorrectionResponseDto response = mapper.toResponseDto(task);

        assertEquals(TaskStatus.COMPLETED, response.getStatus());
        assertEquals("Corrected text", response.getText());
        assertNull(response.getErrorMessage());
    }

    @Test
    void toResponseDto_FailedTask_ReturnsErrorMessage() {
        CorrectionTask task = CorrectionTask.builder()
                .status(TaskStatus.FAILED)
                .errorMessage("API Timeout")
                .build();

        CorrectionResponseDto response = mapper.toResponseDto(task);

        assertEquals(TaskStatus.FAILED, response.getStatus());
        assertEquals("API Timeout", response.getErrorMessage());
        assertNull(response.getText());
    }
}