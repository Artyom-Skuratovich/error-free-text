package com.github.artyomskuratovich.errorfreetext.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.artyomskuratovich.errorfreetext.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleJsonParseExceptions(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        String errorMessage = "Malformed JSON request or syntax error";

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            if (targetType.isEnum()) {
                String allowedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                errorMessage = String.format(
                        "Invalid value '%s' for field '%s'. Allowed values: [%s]",
                        invalidFormatException.getValue(),
                        invalidFormatException.getPath().getFirst().getFieldName(),
                        allowedValues
                );
            } else {
                errorMessage = String.format(
                        "Invalid data type for field '%s'. Expected type: %s",
                        invalidFormatException.getPath().getFirst().getFieldName(),
                        targetType.getSimpleName()
                );
            }
        }

        return createResponseEntity(ErrorCode.INVALID_JSON_FORMAT, errorMessage, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return createResponseEntity(ErrorCode.VALIDATION_ERROR, errorMessage, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundExceptions(
            EntityNotFoundException exception,
            HttpServletRequest request
    ) {
        return createResponseEntity(ErrorCode.ENTITY_NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtExceptions(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception at path: {}", request.getRequestURI(), exception);
        return createResponseEntity(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    private ResponseEntity<ErrorResponseDto> createResponseEntity(
            ErrorCode errorCode,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseBody = ErrorResponseDto.builder()
                .errorMessage(message)
                .errorCode(errorCode.getCode())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(responseBody);
    }
}