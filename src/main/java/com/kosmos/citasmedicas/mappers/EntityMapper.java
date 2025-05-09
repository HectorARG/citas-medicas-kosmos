package com.kosmos.citasmedicas.mappers;

import java.util.List;

/**
 * Interfaz genérica para mappers de entidad a DTO y viceversa.
 * D: Tipo de DTO (Data Transfer Object)
 * E: Tipo de Entidad
 */
public interface EntityMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
    List<E> toEntity(List<D> dtoList);
    List<D> toDto(List<E> entityList);
}
