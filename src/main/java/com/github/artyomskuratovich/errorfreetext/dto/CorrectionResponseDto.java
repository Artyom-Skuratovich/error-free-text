package com.github.artyomskuratovich.errorfreetext.dto;

import com.github.artyomskuratovich.errorfreetext.model.TaskStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionResponseDto {
    private TaskStatus status;
    private String text;
    private String errorMessage;
}