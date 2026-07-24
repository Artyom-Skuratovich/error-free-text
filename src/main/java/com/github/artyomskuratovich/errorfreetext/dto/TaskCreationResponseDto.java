package com.github.artyomskuratovich.errorfreetext.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreationResponseDto {
    private UUID taskId;
}