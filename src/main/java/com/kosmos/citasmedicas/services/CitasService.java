package com.kosmos.citasmedicas.services;

import com.kosmos.citasmedicas.models.dto.CitaDto;
import com.kosmos.citasmedicas.models.dto.CitaRequestDto;
import com.kosmos.citasmedicas.models.dto.ConsultorioDto;
import com.kosmos.citasmedicas.models.dto.DoctorDto;

import java.time.LocalDate;
import java.util.List;

public interface CitasService {
    CitaDto createAppointment(CitaRequestDto appointmentDto);
    CitaDto updateAppointment(Integer id, CitaRequestDto appointmentDto);
    void cancelAppointment(Integer id);
    CitaDto getAppointmentDetails(Integer id);
    List<CitaDto> findAppointments(LocalDate fecha, Integer consultorioId, Integer doctorId);
    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Integer id);
    List<ConsultorioDto> getAllConsultorios();
    ConsultorioDto getConsultorioById(Integer id);
}
