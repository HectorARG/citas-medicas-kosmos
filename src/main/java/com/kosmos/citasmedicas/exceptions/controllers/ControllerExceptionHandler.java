package com.kosmos.citasmedicas.exceptions.controllers;

import com.kosmos.citasmedicas.exceptions.errors.*;
import com.kosmos.citasmedicas.exceptions.models.ExceptionResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Maneja excepciones de Recurso No Encontrado.
     * Retorna estado HTTP 404 Not Found.
     */
    @ExceptionHandler({ResourceNotFoundException.class, AppointmentNotFoundException.class})
    public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponseDTO errorResponse = ExceptionResponseDTO.builder()
                .message(ex.getMessage())
                .title(status.getReasonPhrase().toUpperCase())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Maneja excepciones de Conflicto de Negocio (Validaciones de Alta de Cita).
     * Incluye SameTimeInConsultorioException, SameTimeForDoctorException,
     * PatientTimeConflictException, DoctorDailyLimitExceededException.
     * Retorna estado HTTP 409 Conflict.
     */
    @ExceptionHandler({
            SameTimeInConsultorioException.class,
            SameTimeForDoctorException.class,
            PatientTimeConflictException.class,
            DoctorDailyLimitExceededException.class,
            AppointmentCancellationException.class,
            AppointmentEditException.class
    })
    public ResponseEntity<ExceptionResponseDTO> handleConflictException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionResponseDTO errorResponse = ExceptionResponseDTO.builder()
                .message(ex.getMessage())
                .title(status.getReasonPhrase().toUpperCase())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Maneja errores de validación de Jakarta Bean Validation (@Valid).
     * Retorna estado HTTP 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ExceptionResponseDTO errorResponse = ExceptionResponseDTO.builder()
                .message("Errores de validación: " + errorMessage)
                .title(status.getReasonPhrase().toUpperCase())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
    /**
     * Manejador genérico para cualquier otra RuntimeException no esperada
     **/
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDTO> handleGenericRuntimeException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponseDTO errorResponse = ExceptionResponseDTO.builder()
                .message("Ha ocurrido un error interno inesperado.")
                .title(status.getReasonPhrase().toUpperCase())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

}
