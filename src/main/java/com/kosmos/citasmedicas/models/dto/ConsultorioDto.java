package com.kosmos.citasmedicas.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioDto {
    private Integer id;
    private String numeroConsultorio;
    private String piso;
}
