package com.clinica.aauca.controller;

import com.clinica.aauca.dao.*;
import com.clinica.aauca.model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

/**
 * REST Controller for Clínica Aauca Web API.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow frontend to call backend
public class RestApiController {

    private final UserDAO userDAO = new UserDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<User> userOpt = userDAO.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Return safe user data (avoid returning password)
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @GetMapping("/appointments")
    public List<Appointment> getAppointments() {
        return appointmentDAO.findAllWithNames();
    }

    @PostMapping("/appointments/update")
    public ResponseEntity<?> updateAppointmentStatus(@RequestBody Map<String, String> data) {
        int id = Integer.parseInt(data.get("id"));
        String status = data.get("status");
        if (appointmentDAO.updateStatus(id, status)) {
            return ResponseEntity.ok("Actualizado correctamente");
        }
        return ResponseEntity.status(500).body("Error al actualizar");
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("active_patients", 1240);
        stats.put("daily_appointments", 48);
        stats.put("revenue", "2.4M");
        return stats;
    }
}
