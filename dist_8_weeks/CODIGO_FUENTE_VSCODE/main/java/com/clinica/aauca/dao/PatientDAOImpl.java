package com.clinica.aauca.dao;

import com.clinica.aauca.model.Patient;
import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient patient) {
        String sql = "INSERT INTO pacientes (dni, nombre_completo, telefono, email, direccion, fecha_nacimiento) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getDni());
            pstmt.setString(2, patient.getFullName());
            pstmt.setString(3, patient.getPhone());
            pstmt.setString(4, patient.getEmail());
            pstmt.setString(5, patient.getAddress());
            pstmt.setString(6, patient.getBirthDate());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Patient patient) {
        String sql = "UPDATE pacientes SET nombre_completo=?, telefono=?, email=?, direccion=?, fecha_nacimiento=? WHERE dni=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getFullName());
            pstmt.setString(2, patient.getPhone());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getAddress());
            pstmt.setString(5, patient.getBirthDate());
            pstmt.setString(6, patient.getDni());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pacientes WHERE id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Optional<Patient> findByDni(String dni) {
        String sql = "SELECT * FROM pacientes WHERE dni=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Patient> search(String query) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE dni LIKE ? OR nombre_completo LIKE ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) patients.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    private Patient extractFromResultSet(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getInt("id"),
            rs.getString("dni"),
            rs.getString("nombre_completo"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getString("direccion"),
            rs.getString("fecha_nacimiento")
        );
    }
}
