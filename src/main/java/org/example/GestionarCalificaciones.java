package org.example;

import java.sql.*;
import javax.swing.*;

public class GestionarCalificaciones extends JFrame {

    private JTextField cedulaField;
    private JTextField[] gradeFields;
    private JButton saveButton;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mi_base_de_datos";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public GestionarCalificaciones() {
        setTitle("Gestionar Calificaciones");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        cedulaField = new JTextField(20);
        gradeFields = new JTextField[5];  // Para 5 materias

        panel.add(new JLabel("Cédula:"));
        panel.add(cedulaField);

        // Añadimos los campos para ingresar las calificaciones
        for (int i = 0; i < 5; i++) {
            panel.add(new JLabel("Materia " + (i + 1) + ":"));
            gradeFields[i] = new JTextField(5);
            panel.add(gradeFields[i]);
        }

        saveButton = new JButton("Guardar Calificaciones");
        saveButton.addActionListener(e -> saveGrades());
        panel.add(saveButton);

        add(panel);
    }

    private void saveGrades() {
        String cedula = cedulaField.getText();
        double[] grades = new double[5];
        boolean valid = true;

        // Validamos las calificaciones
        for (int i = 0; i < 5; i++) {
            try {
                grades[i] = Double.parseDouble(gradeFields[i].getText());
                if (grades[i] < 0 || grades[i] > 20) {
                    JOptionPane.showMessageDialog(this, "Las calificaciones deben estar entre 0 y 20", "Error", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido en todas las calificaciones", "Error", JOptionPane.ERROR_MESSAGE);
                valid = false;
                break;
            }
        }
        
        if (valid) {
            if (saveGradesToDatabase(cedula, grades)) {
                JOptionPane.showMessageDialog(this, "Calificaciones guardadas correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar las calificaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean saveGradesToDatabase(String cedula, double[] grades) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO estudiantes (cedula, materia1, materia2, materia3, materia4, materia5) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cedula);
            for (int i = 0; i < 5; i++) {
                stmt.setDouble(i + 2, grades[i]);
            }
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

