package com.kosmos.citasmedicas.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "doctores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;
    @Column(nullable = false, length = 100)
    private String especialidad;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Cita> citas;
}
