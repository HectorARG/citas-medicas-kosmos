package com.kosmos.citasmedicas.mappers;

import com.kosmos.citasmedicas.models.dto.ConsultorioDto;
import com.kosmos.citasmedicas.models.entities.Consultorio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultorioMapper extends EntityMapper<ConsultorioDto, Consultorio> { }
