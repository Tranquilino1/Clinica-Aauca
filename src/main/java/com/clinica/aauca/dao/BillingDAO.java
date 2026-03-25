package com.clinica.aauca.dao;

import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;

public class BillingDAO {

    public boolean saveInvoice(int patientId, double subtotal, double tax, double total) {
        String sql = "INSERT INTO facturas (paciente_id, subtotal, impuestos, total, estado) VALUES (?, ?, ?, ?, 'PAGADA')";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setDouble(2, subtotal);
            pstmt.setDouble(3, tax);
            pstmt.setDouble(4, total);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
