package com.kosmos.citasmedicas.repositories;

import com.kosmos.citasmedicas.models.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {}
