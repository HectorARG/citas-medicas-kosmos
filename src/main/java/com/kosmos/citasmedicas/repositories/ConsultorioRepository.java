package com.kosmos.citasmedicas.repositories;

import com.kosmos.citasmedicas.models.entities.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {}
