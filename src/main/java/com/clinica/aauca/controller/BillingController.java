package com.clinica.aauca.controller;

import com.clinica.aauca.dao.*;
import com.clinica.aauca.model.Product;
import com.clinica.aauca.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BillingController implements Initializable {

    @FXML private TableView<Product> tblInventory;
    @FXML private TableColumn<Product, String> colItemName;
    @FXML private TableColumn<Product, Double> colItemPrice;
    @FXML private TableColumn<Product, Integer> colItemStock;
    @FXML private ListView<String> listInvoiceDetails;
    @FXML private Label lblSubtotal, lblTax, lblTotal;
    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private TextField txtSearchProduct;

    private final ProductDAO productDAO = new ProductDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final BillingDAO billingDAO = new BillingDAO();
    
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    
    private double currentSubtotal = 0.0;
    private static final double TAX_RATE = 0.15; // 15% VAT en Guinea Ecuatorial

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadInventory();
        loadPatients();
        setupSearch();
        updateTotal(0);
    }

    private void setupTable() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void loadInventory() {
        productList.setAll(productDAO.findAll());
        tblInventory.setItems(productList);
    }

    private void loadPatients() {
        patientList.setAll(patientDAO.findAll());
        cmbPatient.setItems(patientList);
        
        cmbPatient.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName() + " (DIP: " + item.getDni() + ")");
                }
            }
        });
        
        cmbPatient.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
    }

    private void setupSearch() {
        txtSearchProduct.textProperty().addListener((obs, oldVal, newVal) -> {
            filterProducts(newVal);
        });
    }

    private void filterProducts(String query) {
        if (query == null || query.isEmpty()) {
            tblInventory.setItems(productList);
        } else {
            ObservableList<Product> filtered = FXCollections.observableArrayList();
            for (Product p : productList) {
                if (p.getName().toLowerCase().contains(query.toLowerCase()) || 
                    (p.getCategory() != null && p.getCategory().toLowerCase().contains(query.toLowerCase()))) {
                    filtered.add(p);
                }
            }
            tblInventory.setItems(filtered);
        }
    }

    @FXML
    private void addToInvoice(ActionEvent event) {
        Product p = tblInventory.getSelectionModel().getSelectedItem();
        if (p != null) {
            listInvoiceDetails.getItems().add(p.getName() + " - " + String.format("%.0f", p.getPrice()) + " FCFA");
            updateTotal(p.getPrice());
        }
    }

    private void updateTotal(double delta) {
        currentSubtotal += delta;
        double tax = currentSubtotal * TAX_RATE;
        double total = currentSubtotal + tax;

        lblSubtotal.setText(String.format("%.0f FCFA", currentSubtotal));
        lblTax.setText(String.format("%.0f FCFA (15%%)", tax));
        lblTotal.setText(String.format("%.0f FCFA", total));
    }

    @FXML
    private void clearInvoice(ActionEvent event) {
        listInvoiceDetails.getItems().clear();
        currentSubtotal = 0;
        updateTotal(0);
        cmbPatient.getSelectionModel().clearSelection();
    }

    @FXML
    private void emitInvoice(ActionEvent event) {
        Patient selected = cmbPatient.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Primero seleccione un paciente para la factura.");
            return;
        }
        if (listInvoiceDetails.getItems().isEmpty()) {
            showAlert("Aviso", "La factura no tiene ningún detalle.");
            return;
        }
        
        double tax = currentSubtotal * TAX_RATE;
        double total = currentSubtotal + tax;
        
        if (billingDAO.saveInvoice(selected.getId(), currentSubtotal, tax, total)) {
            generateInvoicePDF(selected, currentSubtotal, tax, total);
            showAlert("Éxito", "Factura emitida y registrada por " + String.format("%.0f", total) + " FCFA para " + selected.getFullName());
            clearInvoice(event);
        } else {
            showAlert("Error", "No se pudo registrar la factura en la base de datos.");
        }
    }

    private void generateInvoicePDF(Patient p, double subtotal, double tax, double total) {
        java.io.File file = new java.io.File("FACTURA_" + System.currentTimeMillis() + ".pdf");
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, fos);
            document.open();

            // Membrete
            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 20);
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("CLINICA AAUCA - FACTURA", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);
            document.add(new com.lowagie.text.Paragraph("Djibloho / Oyala - Guinea Ecuatorial\n\n"));

            // Datos Paciente
            document.add(new com.lowagie.text.Paragraph("PACIENTE: " + p.getFullName()));
            document.add(new com.lowagie.text.Paragraph("DIP: " + p.getDni()));
            document.add(new com.lowagie.text.Paragraph("FECHA: " + new java.util.Date().toString()));
            document.add(new com.lowagie.text.Paragraph("\n------------------------------------------------------------\n"));

            // Detalles
            document.add(new com.lowagie.text.Paragraph("DETALLES:"));
            for (String detail : listInvoiceDetails.getItems()) {
                document.add(new com.lowagie.text.Paragraph(" - " + detail));
            }

            document.add(new com.lowagie.text.Paragraph("\n------------------------------------------------------------\n"));
            document.add(new com.lowagie.text.Paragraph("SUBTOTAL: " + String.format("%.0f", subtotal) + " FCFA"));
            document.add(new com.lowagie.text.Paragraph("IVA (15%): " + String.format("%.0f", tax) + " FCFA"));
            document.add(new com.lowagie.text.Paragraph("TOTAL FINAL: " + String.format("%.0f", total) + " FCFA", com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 14)));

            document.close();
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
