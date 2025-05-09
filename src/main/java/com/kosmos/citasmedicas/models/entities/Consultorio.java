package com.kosmos.citasmedicas.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "consultorios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "numero_consultorio", nullable = false, unique = true, length = 10)
    private String numeroConsultorio;
    @Column(nullable = false, length = 10)
    private String piso;
    @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Cita> citas;
}
