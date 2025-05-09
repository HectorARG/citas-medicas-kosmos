package com.kosmos.citasmedicas.mappers;

import com.kosmos.citasmedicas.models.dto.DoctorDto;
import com.kosmos.citasmedicas.models.entities.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper extends EntityMapper<DoctorDto, Doctor> { }
