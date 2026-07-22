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
    @Size(min = 3, message = "Текст должен содержать минимум 3 символа")
    @Pattern(
            regexp = "^[^\\p{L}]*\\p{L}.*$",
            message = "Текст не может содержать только спецсимволы и цифры"
    )
    private String text;

    @NotNull(message = "Необходимо указать язык текста")
    private Language language;
}