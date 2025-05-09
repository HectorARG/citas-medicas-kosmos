package com.kosmos.citasmedicas.services;

import com.kosmos.citasmedicas.exceptions.errors.*;
import com.kosmos.citasmedicas.mappers.CitaMapper;
import com.kosmos.citasmedicas.mappers.ConsultorioMapper;
import com.kosmos.citasmedicas.mappers.DoctorMapper;
import com.kosmos.citasmedicas.models.dto.CitaDto;
import com.kosmos.citasmedicas.models.dto.CitaRequestDto;
import com.kosmos.citasmedicas.models.dto.ConsultorioDto;
import com.kosmos.citasmedicas.models.dto.DoctorDto;
import com.kosmos.citasmedicas.models.entities.Cita;
import com.kosmos.citasmedicas.models.entities.Consultorio;
import com.kosmos.citasmedicas.models.entities.Doctor;
import com.kosmos.citasmedicas.repositories.CitaRepository;
import com.kosmos.citasmedicas.repositories.ConsultorioRepository;
import com.kosmos.citasmedicas.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitasServiceImpl implements CitasService {

    private static final int MAX_APPOINTMENTS_PER_DOCTOR_PER_DAY = 8;

    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultorioRepository consultorioRepository;
    private final CitaMapper citaMapper;
    private final DoctorMapper doctorMapper;
    private final ConsultorioMapper consultorioMapper;

    @Override
    @Transactional
    public CitaDto createAppointment(CitaRequestDto appointmentDto) {
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + appointmentDto.getDoctorId()));

        Consultorio consultorio = consultorioRepository.findById(appointmentDto.getConsultorioId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + appointmentDto.getConsultorioId()));

        LocalDateTime requestedTime = appointmentDto.getFechaHora();
        LocalDate requestedDate = requestedTime.toLocalDate();

        LocalDateTime startOfDay = requestedDate.atStartOfDay();
        LocalDateTime endOfDay = requestedDate.plusDays(1).atStartOfDay().minusNanos(1);

        citaRepository.findByConsultorioAndFechaHora(consultorio, requestedTime)
                .ifPresent(cita -> {
                    throw new SameTimeInConsultorioException("Ya existe una cita en el consultorio " + consultorio.getNumeroConsultorio() + " a las " + requestedTime);
                });
        citaRepository.findByDoctorAndFechaHora(doctor, requestedTime)
                .ifPresent(cita -> {
                    throw new SameTimeForDoctorException("El doctor " + doctor.getNombre() + " ya tiene una cita agendada a las " + requestedTime);
                });
        List<Cita> citasPacienteHoy = citaRepository.findByNombrePacienteAndFechaOrderByFechaHora(appointmentDto.getNombrePaciente(), startOfDay, endOfDay);
        for (Cita citaPaciente : citasPacienteHoy) {
            LocalDateTime existingTime = citaPaciente.getFechaHora();
            if (requestedTime.equals(existingTime) ||
                    (requestedTime.isAfter(existingTime) && requestedTime.minusHours(2).isBefore(existingTime)) ||
                    (requestedTime.isBefore(existingTime) && requestedTime.plusHours(2).isAfter(existingTime))
            )
            {
                throw new PatientTimeConflictException("El paciente " + appointmentDto.getNombrePaciente() + " ya tiene una cita agendada el mismo día que entra en conflicto horario a las " + existingTime);
            }
        }

        long citasDoctorHoyCount = citaRepository.countByDoctorAndFecha(doctor, startOfDay, endOfDay);

        if (citasDoctorHoyCount >= MAX_APPOINTMENTS_PER_DOCTOR_PER_DAY) {
            throw new DoctorDailyLimitExceededException("El doctor " + doctor.getNombre() + " ya tiene el máximo de " + MAX_APPOINTMENTS_PER_DOCTOR_PER_DAY + " citas agendadas para el " + requestedDate);
        }

        Cita nuevaCita = citaMapper.toEntity(appointmentDto);
        nuevaCita.setDoctor(doctor);
        nuevaCita.setConsultorio(consultorio);
        nuevaCita.setEstado("PROGRAMADA");

        Cita savedCita = citaRepository.save(nuevaCita);
        return citaMapper.toDto(savedCita);
    }

    @Override
    @Transactional
    public CitaDto updateAppointment(Integer id, CitaRequestDto appointmentDto) {
        Cita existingCita = citaRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + id));

        Doctor originalDoctor = existingCita.getDoctor();
        Consultorio originalConsultorio = existingCita.getConsultorio();

        Doctor newDoctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + appointmentDto.getDoctorId()));

        Consultorio newConsultorio = consultorioRepository.findById(appointmentDto.getConsultorioId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + appointmentDto.getConsultorioId()));

        LocalDateTime originalTime = existingCita.getFechaHora();
        String originalPacienteNombre = existingCita.getNombrePaciente();

        LocalDateTime effectiveNewTime = (appointmentDto.getFechaHora() != null) ? appointmentDto.getFechaHora() : originalTime;
        String effectiveNewPacienteNombre =
                (appointmentDto.getNombrePaciente() != null &&
                        !appointmentDto.getNombrePaciente().isEmpty() &&
                        !appointmentDto.getNombrePaciente().isBlank()) ? appointmentDto.getNombrePaciente() : originalPacienteNombre;

        boolean timeChanged = !effectiveNewTime.equals(originalTime);
        boolean doctorChanged = !newDoctor.equals(originalDoctor);
        boolean consultorioChanged = !newConsultorio.equals(originalConsultorio);
        boolean patientNameChanged = !effectiveNewPacienteNombre.equals(originalPacienteNombre);

        if (timeChanged || doctorChanged || consultorioChanged || patientNameChanged) {

            LocalDate effectiveNewDate = effectiveNewTime.toLocalDate();
            LocalDateTime startOfEffectiveNewDay = effectiveNewDate.atStartOfDay();
            LocalDateTime endOfEffectiveNewDay = effectiveNewDate.plusDays(1).atStartOfDay().minusNanos(1);

            if (consultorioChanged || timeChanged) {
                Optional<Cita> conflictConsultorio = citaRepository.findByConsultorioAndFechaHora(newConsultorio, effectiveNewTime);
                if (conflictConsultorio.isPresent() && !conflictConsultorio.get().getId().equals(id)) {
                    throw new SameTimeInConsultorioException("Ya existe otra cita en el consultorio " + newConsultorio.getNumeroConsultorio() + " a las " + effectiveNewTime);
                }
            }

            if (doctorChanged || timeChanged) {
                Optional<Cita> conflictDoctor = citaRepository.findByDoctorAndFechaHora(newDoctor, effectiveNewTime);
                if (conflictDoctor.isPresent() && !conflictDoctor.get().getId().equals(id)) {
                    throw new SameTimeForDoctorException("El doctor " + newDoctor.getNombre() + " ya tiene otra cita agendada a las " + effectiveNewTime);
                }
            }

            if (patientNameChanged || timeChanged) {

                List<Cita> citasPacienteEnNuevoDia = citaRepository.findByNombrePacienteAndFechaOrderByFechaHora(effectiveNewPacienteNombre, startOfEffectiveNewDay, endOfEffectiveNewDay);

                for (Cita citaPaciente : citasPacienteEnNuevoDia) {
                    LocalDateTime existingTime = citaPaciente.getFechaHora();
                    if (effectiveNewTime.equals(existingTime) ||
                            (effectiveNewTime.isAfter(existingTime) && effectiveNewTime.minusHours(2).isBefore(existingTime)) ||
                            (effectiveNewTime.isBefore(existingTime) && effectiveNewTime.plusHours(2).isAfter(existingTime))
                    )
                    {
                        throw new PatientTimeConflictException("La actualización entra en conflicto horario con la cita del paciente " + effectiveNewPacienteNombre + " a las " + existingTime + " en la misma fecha.");
                    }
                }
            }

            if (doctorChanged || timeChanged) {
                long citasDoctorHoyCount = citaRepository.countByDoctorAndFecha(newDoctor,startOfEffectiveNewDay,endOfEffectiveNewDay);

                if (citasDoctorHoyCount >= MAX_APPOINTMENTS_PER_DOCTOR_PER_DAY) {
                    throw new DoctorDailyLimitExceededException("Actualizar esta cita excede el límite diario de " + MAX_APPOINTMENTS_PER_DOCTOR_PER_DAY + " citas para el doctor " + newDoctor.getNombre() + " en la fecha " + effectiveNewDate);
                }
            }
        }

        citaMapper.updateCitaFromDto(appointmentDto, existingCita);

        if (doctorChanged) { existingCita.setDoctor(newDoctor); }
        if (consultorioChanged) { existingCita.setConsultorio(newConsultorio); }

        Cita updatedCita = citaRepository.save(existingCita);

        return citaMapper.toDto(updatedCita);
    }

    @Override
    @Transactional
    public void cancelAppointment(Integer id) {
        Cita citaToCancel = citaRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + id));
        if (citaToCancel.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new AppointmentCancellationException("No se puede cancelar una cita porque su horario ya ha pasado.");
        }
        citaToCancel.setEstado("CANCELADA");
        citaRepository.save(citaToCancel);
    }

    @Override
    @Transactional(readOnly = true)
    public CitaDto getAppointmentDetails(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + id));
        return citaMapper.toDto(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDto> findAppointments(LocalDate fecha, Integer consultorioId, Integer doctorId) {
        LocalDateTime startOfDay = fecha.atStartOfDay();
        LocalDateTime endOfDay = fecha.plusDays(1).atStartOfDay().minusNanos(1);

        List<Cita> citas = new ArrayList<>();
        if (consultorioId != null && doctorId != null) {
            consultorioRepository.findById(consultorioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + consultorioId));
            doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + doctorId));
            citas = citaRepository.findByFechaHoraBetween(startOfDay, endOfDay).stream()
                    .filter(c -> c.getConsultorio().getId().equals(consultorioId) && c.getDoctor().getId().equals(doctorId)).toList();
        } else if (doctorId == null) {
            Consultorio consultorio = consultorioRepository.findById(consultorioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + consultorioId));
            citas = citaRepository.findByConsultorioAndFechaHoraBetween(consultorio, startOfDay, endOfDay);
        }

        return citaMapper.toDto(citas);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.toDto(doctors);
    }

    @Override
    public DoctorDto getDoctorById(Integer id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + id));
        return doctorMapper.toDto(doctor);
    }

    @Override
    public List<ConsultorioDto> getAllConsultorios() {
        List<Consultorio> consultorios = consultorioRepository.findAll();
        return consultorioMapper.toDto(consultorios);
    }

    @Override
    public ConsultorioDto getConsultorioById(Integer id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));
        return consultorioMapper.toDto(consultorio);
    }
}
