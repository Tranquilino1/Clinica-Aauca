package com.clinica.aauca.dao;

import com.clinica.aauca.model.MedicalHistory;
import java.util.List;

public interface MedicalHistoryDAO {
    boolean save(MedicalHistory history);
    List<MedicalHistory> findByPatientId(int patientId);
}
