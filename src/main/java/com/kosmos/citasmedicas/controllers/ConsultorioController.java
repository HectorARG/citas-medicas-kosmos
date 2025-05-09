package com.kosmos.citasmedicas.controllers;

import com.kosmos.citasmedicas.models.dto.ConsultorioDto;
import com.kosmos.citasmedicas.services.CitasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("consultorios")
public class ConsultorioController {

    private final CitasService citasService;

    @GetMapping
    public ResponseEntity<List<ConsultorioDto>> getAllConsultorios() {
        List<ConsultorioDto> consultorios = citasService.getAllConsultorios();
        return ResponseEntity.ok(consultorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioDto> getConsultorioById(@PathVariable Integer id) {
        ConsultorioDto consultorio = citasService.getConsultorioById(id);
        return ResponseEntity.ok(consultorio);
    }
}
