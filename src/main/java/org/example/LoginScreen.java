package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pruebab2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginScreen() {
        // Configura la interfaz gráfica del login
        panel = new JPanel();
        panel.setLayout(new GridLayout(10, 12));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        // Configura la acción del botón de login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (validateCredentials(username, password)) {
                JOptionPane.showMessageDialog(panel, "Ingreso al sistema exitoso");
                openGestionarCalificaciones();
            } else {
                JOptionPane.showMessageDialog(panel, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public JPanel getLoginScreen() {
        return panel;
    }

    private static boolean validateCredentials(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void openGestionarCalificaciones() {
        GestionarCalificaciones gestionarFrame = new GestionarCalificaciones();
        gestionarFrame.setVisible(true);
    }
}

