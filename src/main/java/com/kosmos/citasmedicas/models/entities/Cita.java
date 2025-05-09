package com.kosmos.citasmedicas.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultorio_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CITA_CONSULTORIO"))
    private Consultorio consultorio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CITA_DOCTOR"))
    private Doctor doctor;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(name = "nombre_paciente", nullable = false, length = 100)
    private String nombrePaciente;
    @Column(nullable = false, length = 20)
    private String estado;
}
