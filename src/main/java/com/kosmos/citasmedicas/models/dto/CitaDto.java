package com.kosmos.citasmedicas.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaDto {
    private Integer id;
    private Integer consultorioId;
    private String consultorioNumero;
    private String consultorioPiso;
    private Integer doctorId;
    private String doctorNombreCompleto;
    private String doctorEspecialidad;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaHora;
    private String nombrePaciente;
    private String estado;
}
