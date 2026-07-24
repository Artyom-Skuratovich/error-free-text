package com.github.artyomskuratovich.errorfreetext.dto;

import com.github.artyomskuratovich.errorfreetext.model.Language;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionRequestDto {
    @Size(min = 3, message = "Text must contain at least 3 characters")
    @Pattern(
            regexp = "^[^\\p{L}]*\\p{L}.*$",
            message = "Text cannot consist only of special characters and numbers"
    )
    private String text;

    @NotNull(message = "Text language must be specified")
    private Language language;
}