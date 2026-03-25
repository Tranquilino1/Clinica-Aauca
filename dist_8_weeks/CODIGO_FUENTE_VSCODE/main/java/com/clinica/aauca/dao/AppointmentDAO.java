package com.clinica.aauca.dao;

import com.clinica.aauca.model.Appointment;
import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface AppointmentDAO {
    boolean save(Appointment appt);
    List<Appointment> findByDate(String date);
    List<Appointment> findAllWithNames();
    boolean updateStatus(int id, String status);
}

class AppointmentDAOImpl implements AppointmentDAO {
    @Override
    public boolean save(Appointment appt) {
        String sql = "INSERT INTO citas (paciente_id, medico_id, fecha_cita, hora_cita, motivo) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setString(3, appt.getDate());
            pstmt.setString(4, appt.getTime());
            pstmt.setString(5, appt.getReason());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Appointment> findAllWithNames() {
        List<Appointment> appts = new ArrayList<>();
        String sql = "SELECT c.*, p.nombre_completo as paciente, u.nombre_completo as medico " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN usuarios u ON c.medico_id = u.id";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Appointment a = new Appointment(
                    rs.getInt("id"), rs.getInt("paciente_id"), rs.getInt("medico_id"),
                    rs.getString("fecha_cita"), rs.getString("hora_cita"),
                    rs.getString("estado"), rs.getString("motivo")
                );
                a.setPatientName(rs.getString("paciente"));
                a.setDoctorName(rs.getString("medico"));
                appts.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appts;
    }

    @Override
    public List<Appointment> findByDate(String date) { return new ArrayList<>(); }
    @Override
    public boolean updateStatus(int id, String status) { return false; }
}
