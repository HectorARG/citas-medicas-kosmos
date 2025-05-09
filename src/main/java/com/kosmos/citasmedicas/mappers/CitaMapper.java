package com.kosmos.citasmedicas.mappers;

import com.kosmos.citasmedicas.models.dto.CitaDto;
import com.kosmos.citasmedicas.models.dto.CitaRequestDto;
import com.kosmos.citasmedicas.models.entities.Cita;
import com.kosmos.citasmedicas.models.entities.Doctor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CitaMapper {

    @Mapping(source = "consultorio.id", target = "consultorioId")
    @Mapping(source = "consultorio.numeroConsultorio", target = "consultorioNumero")
    @Mapping(source = "consultorio.piso", target = "consultorioPiso")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor", target = "doctorNombreCompleto", qualifiedByName = "doctorToFullName")
    @Mapping(source = "doctor.especialidad", target = "doctorEspecialidad")
    CitaDto toDto(Cita entity);

    List<CitaDto> toDto(List<Cita> entityList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultorio", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Cita toEntity(CitaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultorio", ignore = true)
    void updateCitaFromDto(CitaRequestDto dto, @MappingTarget Cita entity);

    @Named("doctorToFullName")
    default String doctorToFullName(Doctor doctor) {
        if (doctor == null) { return null; }
        return doctor.getNombre() + " " + doctor.getApellidoPaterno() + " " + doctor.getApellidoMaterno();
    }

}
