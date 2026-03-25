package com.clinica.aauca.dao;

import com.clinica.aauca.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    boolean save(Patient patient);
    boolean update(Patient patient);
    boolean delete(int id);
    List<Patient> findAll();
    Optional<Patient> findByDni(String dni);
    List<Patient> search(String query);
}
