package com.kosmos.citasmedicas.repositories;

import com.kosmos.citasmedicas.models.entities.Cita;
import com.kosmos.citasmedicas.models.entities.Consultorio;
import com.kosmos.citasmedicas.models.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    Optional<Cita> findByConsultorioAndFechaHora(Consultorio consultorio, LocalDateTime fechaHora);

    Optional<Cita> findByDoctorAndFechaHora(Doctor doctor, LocalDateTime fechaHora);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.doctor = :doctor AND c.fechaHora BETWEEN :startOfDay AND :endOfDay")
    long countByDoctorAndFecha(
            @Param("doctor") Doctor doctor,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT c FROM Cita c WHERE c.nombrePaciente = :nombrePaciente AND c.fechaHora BETWEEN :startOfDay AND :endOfDay ORDER BY c.fechaHora")
    List<Cita> findByNombrePacienteAndFechaOrderByFechaHora(
            @Param("nombrePaciente") String nombrePaciente,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    List<Cita> findByConsultorioAndFechaHoraBetween(Consultorio consultorio, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Cita> findByFechaHoraBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Cita> findById(Integer id);
}
