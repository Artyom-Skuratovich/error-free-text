package com.github.artyomskuratovich.errorfreetext.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionTask {
    private long id;

    private String text;

    private String errorMessage;

    private Language language;

    private TaskStatus status;
}