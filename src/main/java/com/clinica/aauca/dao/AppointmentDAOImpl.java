package com.clinica.aauca.dao;

import com.clinica.aauca.model.Appointment;
import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean save(Appointment appt) {
        String sql = "INSERT INTO citas (paciente_id, medico_id, fecha_cita, hora_cita, estado, motivo) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setString(3, appt.getDate());
            pstmt.setString(4, appt.getTime());
            pstmt.setString(5, appt.getStatus());
            pstmt.setString(6, appt.getReason());
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
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE citas SET estado=? WHERE id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM citas WHERE id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
