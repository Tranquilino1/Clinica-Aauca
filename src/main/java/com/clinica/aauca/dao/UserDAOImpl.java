package com.clinica.aauca.dao;

import com.clinica.aauca.model.User;
import com.clinica.aauca.util.DatabaseConnector;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementación de la Capa de Acceso a Datos (DAO) para Usuarios usando SQLite.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("nombre_completo"),
                    rs.getString("rol")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean authenticate(String username, String password) {
        return login(username, password).isPresent();
    }

    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    return Optional.of(user);
                }
            } catch (Exception e) {
                System.err.println("Error al verificar password: " + e.getMessage());
            }
        }
        return Optional.empty();
    }
}
