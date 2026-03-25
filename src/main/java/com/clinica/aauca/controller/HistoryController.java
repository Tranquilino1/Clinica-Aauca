package com.clinica.aauca.controller;

import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.dao.MedicalHistoryDAO;
import com.clinica.aauca.dao.MedicalHistoryDAOImpl;
import com.clinica.aauca.model.Patient;
import com.clinica.aauca.model.MedicalHistory;
import com.clinica.aauca.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Element;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML private TextField txtSearchPatient;
    @FXML private ListView<Patient> listPatients;
    @FXML private Label lblPatientName;
    @FXML private TextArea txtReason, txtDiagnosis, txtTreatment;
    @FXML private Button btnPDF, btnReceta;
    @FXML private ListView<MedicalHistory> listHistoryEntries;

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final MedicalHistoryDAO historyDAO = new MedicalHistoryDAOImpl();
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private final ObservableList<MedicalHistory> historyEntries = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        loadPatients();
        
        listPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblPatientName.setText("EXPEDIENTE: " + newVal.getFullName().toUpperCase());
                btnPDF.setDisable(false);
                btnReceta.setDisable(false);
                loadHistory(newVal.getId());
                clearForm();
            }
        });

        listHistoryEntries.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayHistoryEntry(newVal);
            }
        });

        txtSearchPatient.textProperty().addListener((obs, oldVal, newVal) -> {
            filterPatients(newVal);
        });
    }

    private void setupListView() {
        listPatients.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getFullName() + "\n(DIP: " + item.getDni() + ")");
                    setStyle("-fx-font-weight: bold; -fx-padding: 8 12;");
                }
            }
        });

        listHistoryEntries.setCellFactory(param -> new ListCell<MedicalHistory>() {
            @Override
            protected void updateItem(MedicalHistory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDate() + "\n" + (item.getReason().length() > 20 ? item.getReason().substring(0, 20) + "..." : item.getReason()));
                }
            }
        });
    }

    private void loadPatients() {
        patients.setAll(patientDAO.findAll());
        listPatients.setItems(patients);
    }

    private void filterPatients(String query) {
        if (query == null || query.isEmpty()) {
            listPatients.setItems(patients);
        } else {
            ObservableList<Patient> filtered = FXCollections.observableArrayList();
            for (Patient p : patients) {
                if (p.getFullName().toLowerCase().contains(query.toLowerCase()) || 
                    p.getDni().contains(query)) {
                    filtered.add(p);
                }
            }
            listPatients.setItems(filtered);
        }
    }

    private void loadHistory(int patientId) {
        historyEntries.setAll(historyDAO.findByPatientId(patientId));
        listHistoryEntries.setItems(historyEntries);
        
        if (!historyEntries.isEmpty()) {
            displayHistoryEntry(historyEntries.get(0));
        }
    }

    private void displayHistoryEntry(MedicalHistory entry) {
        txtReason.setText(entry.getReason());
        txtDiagnosis.setText(entry.getDiagnosis());
        txtTreatment.setText(entry.getTreatment());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        Patient selected = listPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Seleccione un paciente primero");
            return;
        }

        if (txtReason.getText().isEmpty() || txtDiagnosis.getText().isEmpty()) {
            showAlert("Campos Requeridos", "El motivo y el diagnóstico son obligatorios.");
            return;
        }

        MedicalHistory history = new MedicalHistory();
        history.setPatientId(selected.getId());
        if (SessionManager.getCurrentUser() != null) {
            history.setDoctorId(SessionManager.getCurrentUser().getId());
        } else {
            history.setDoctorId(1);
        }
        history.setReason(txtReason.getText());
        history.setDiagnosis(txtDiagnosis.getText());
        history.setTreatment(txtTreatment.getText());

        if (historyDAO.save(history)) {
            showAlert("Éxito", "Consulta guardada correctamente.");
            loadHistory(selected.getId());
        } else {
            showAlert("Error", "No se pudo guardar la consulta.");
        }
    }

    @FXML
    private void handleExportPDF(ActionEvent event) {
        Patient p = listPatients.getSelectionModel().getSelectedItem();
        if (p == null) return;

        File file = new File("FICHA_MEDICA_" + p.getDni() + ".pdf");
        
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            // Cabecera
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLUE);
            Paragraph title = new Paragraph("CLINICA AAUCA - FICHA MEDICA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Datos Paciente
            Paragraph pData = new Paragraph("PACIENTE: " + p.getFullName());
            pData.add("\nDIP: " + p.getDni());
            pData.add("\nFECHA IMPRESION: " + new Date().toString());
            document.add(pData);
            document.add(new Paragraph("\n--------------------------------------------------------------------------\n\n"));

            // Historial
            for (MedicalHistory h : historyEntries) {
                Paragraph hHeader = new Paragraph("FECHA: " + h.getDate(), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
                document.add(hHeader);
                document.add(new Paragraph("MOTIVO: " + h.getReason()));
                document.add(new Paragraph("DIAGNOSTICO: " + h.getDiagnosis()));
                document.add(new Paragraph("TRATAMIENTO: " + h.getTreatment()));
                document.add(new Paragraph("\n"));
            }

            document.close();
            showAlert("Éxito", "PDF generado en: " + file.getAbsolutePath());
            
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudo generar el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportReceta(ActionEvent event) {
        Patient p = listPatients.getSelectionModel().getSelectedItem();
        if (p == null) return;

        File file = new File("RECETA_" + p.getDni() + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLUE.darker());
            Paragraph title = new Paragraph("RECETA MEDICA - CLINICA AAUCA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("PACIENTE: " + p.getFullName()));
            document.add(new Paragraph("DIP: " + p.getDni()));
            document.add(new Paragraph("FECHA: " + new Date().toString()));
            document.add(new Paragraph("\n------------------------------------------------------------\n"));

            document.add(new Paragraph("PRESCRIPCION / TRATAMIENTO:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(new Paragraph(txtTreatment.getText()));
            document.add(new Paragraph("\n\n\n\n"));
            
            Paragraph footer = new Paragraph("__________________________\nFirma y Sello Médico");
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            showAlert("Éxito", "Receta generada: " + file.getAbsolutePath());
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudo generar la receta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNew(ActionEvent event) {
        clearForm();
        txtReason.requestFocus();
    }

    private void clearForm() {
        txtReason.clear();
        txtDiagnosis.clear();
        txtTreatment.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
