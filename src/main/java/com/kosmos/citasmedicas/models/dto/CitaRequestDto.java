package com.kosmos.citasmedicas.models.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequestDto {
    @NotNull(message = "El ID del consultorio es requerido")
    private Integer consultorioId;
    @NotNull(message = "El ID del doctor es requerido")
    private Integer doctorId;
    @NotNull(message = "La fecha y hora de la cita son requeridas")
    @FutureOrPresent(message = "La fecha y hora de la cita deben ser en el presente o futuro")
    private LocalDateTime fechaHora;
    @NotBlank(message = "El nombre del paciente es requerido")
    @Size(max = 100, message = "El nombre del paciente no puede exceder los 100 caracteres")
    private String nombrePaciente;
}
