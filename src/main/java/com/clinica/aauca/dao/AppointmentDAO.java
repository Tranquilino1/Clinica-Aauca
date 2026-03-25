package com.clinica.aauca.dao;

import com.clinica.aauca.model.Appointment;
import com.clinica.aauca.util.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface AppointmentDAO {
    boolean save(Appointment appt);
    List<Appointment> findAllWithNames();
    boolean updateStatus(int id, String status);
    boolean delete(int id);
}
