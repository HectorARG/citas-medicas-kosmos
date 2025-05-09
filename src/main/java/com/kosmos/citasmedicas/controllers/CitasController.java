package com.kosmos.citasmedicas.controllers;

import com.kosmos.citasmedicas.models.dto.CitaDto;
import com.kosmos.citasmedicas.models.dto.CitaRequestDto;
import com.kosmos.citasmedicas.services.CitasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("citas-medicas")
public class CitasController {

    private final CitasService citasService;

    /**
     * Endpoint para crear una nueva cita.
     */
    @PostMapping
    public ResponseEntity<CitaDto> createAppointment(@Valid @RequestBody CitaRequestDto appointmentDto) {
        CitaDto createdCita = citasService.createAppointment(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCita);
    }

    /**
     * Endpoint para actualizar una cita.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CitaDto> updateAppointment(@PathVariable Integer id, @Valid @RequestBody CitaRequestDto appointmentDto) {
        CitaDto updatedCita = citasService.updateAppointment(id, appointmentDto);
        return ResponseEntity.ok(updatedCita);
    }

    /**
     * Endpoint para cancelar una cita.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Integer id) {
        citasService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para obtener los detalles de una cita.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> getAppointmentDetails(@PathVariable Integer id) {
        // AppointmentNotFoundException será manejada por el @RestControllerAdvice
        CitaDto cita = citasService.getAppointmentDetails(id);
        return ResponseEntity.ok(cita); // Retorna 200 OK
    }

    /**
     * Endpoint para consultar citas con filtros.
     * Filtros opcionales: ?fecha=YYYY-MM-DD&consultorioId=X&doctorId=Y
     */
    @GetMapping
    public ResponseEntity<List<CitaDto>> findAppointments(
            @RequestParam(required = true) LocalDate fecha,
            @RequestParam(required = false) Integer consultorioId,
            @RequestParam(required = false) Integer doctorId) {
        List<CitaDto> citas = citasService.findAppointments(fecha, consultorioId, doctorId);
        return ResponseEntity.ok(citas);
    }

    /**
     * Endpoint para consultar citas por consultorio y fecha.
     */
     @GetMapping("/consultorio/{consultorioId}/fecha/{dateStr}")
     public ResponseEntity<List<CitaDto>> getAppointmentsByConsultorioAndDate(
            @PathVariable Integer consultorioId,
            @PathVariable String dateStr) {
         LocalDate fecha = LocalDate.parse(dateStr);
         List<CitaDto> citas = citasService.findAppointments(fecha, consultorioId, null);
         return ResponseEntity.ok(citas);
     }
}
