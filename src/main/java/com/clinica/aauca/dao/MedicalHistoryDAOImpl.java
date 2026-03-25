package com.clinica.aauca.dao;

import com.clinica.aauca.model.MedicalHistory;
import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryDAOImpl implements MedicalHistoryDAO {
    @Override
    public boolean save(MedicalHistory history) {
        String sql = "INSERT INTO historias_clinicas (paciente_id, medico_id, motivo_consulta, diagnostico, tratamiento) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, history.getPatientId());
            pstmt.setInt(2, history.getDoctorId());
            pstmt.setString(3, history.getReason());
            pstmt.setString(4, history.getDiagnosis());
            pstmt.setString(5, history.getTreatment());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MedicalHistory> findByPatientId(int patientId) {
        List<MedicalHistory> histories = new ArrayList<>();
        String sql = "SELECT * FROM historias_clinicas WHERE paciente_id = ? ORDER BY fecha_consulta DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                histories.add(new MedicalHistory(
                    rs.getInt("id"), rs.getInt("paciente_id"), rs.getInt("medico_id"),
                    rs.getString("fecha_consulta"), rs.getString("motivo_consulta"),
                    rs.getString("diagnostico"), rs.getString("tratamiento")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return histories;
    }
}
