package com.clinica.aauca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

/**
 * Gestor de conexión a la base de datos SQLite.
 */
public class DatabaseConnector {
    private static final String URL = "jdbc:sqlite:clinica_aauca.db";
    private static Connection connection = null;

    private DatabaseConnector() {
        // Singleton
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Forzar carga del driver JDBC de SQLite
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                initializeDatabase();
            } catch (ClassNotFoundException e) {
                System.err.println("Driver SQLite no encontrado.");
                throw new SQLException(e);
            }
        }
        return connection;
    }

    /**
     * Inicializa las tablas si no existen leyendo el archivo schema.sql.
     */
    private static void initializeDatabase() {
        if (connection == null) return;
        
        try (Statement stmt = connection.createStatement()) {
            // Logica para leer el esquema desde resources
            InputStream is = DatabaseConnector.class.getResourceAsStream("/com/clinica/aauca/sql/schema.sql");
            if (is == null) {
                System.err.println("No se pudo cargar el archivo schema.sql.");
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    // Limpiar la línea eliminando comentarios -- y espacios extra
                    String cleanedLine = line.split("--")[0].trim();
                    if (cleanedLine.isEmpty()) continue;
                    
                    sql.append(cleanedLine).append(" ");
                    
                    if (cleanedLine.endsWith(";")) {
                        stmt.execute(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
            System.out.println("Base de datos inicializada correctamente.");
            
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
