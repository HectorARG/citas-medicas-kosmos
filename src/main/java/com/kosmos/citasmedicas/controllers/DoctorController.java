package com.kosmos.citasmedicas.controllers;

import com.kosmos.citasmedicas.models.dto.DoctorDto;
import com.kosmos.citasmedicas.services.CitasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("doctores")
public class DoctorController {

    private final CitasService citasService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = citasService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Integer id) {
        DoctorDto doctor = citasService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }
}
