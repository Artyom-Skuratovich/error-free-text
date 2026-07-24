package com.github.artyomskuratovich.errorfreetext.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private String errorMessage;
    private int errorCode;
    private LocalDateTime timestamp;
    private String path;
}