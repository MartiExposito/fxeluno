package com.java.fx;

import com.java.fx.entidades.Usuario;
import com.java.fx.entidades.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
public class Controller implements Initializable {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @FXML
    private Button loginButton; // Agrega este campo

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel; // Etiqueta para mostrar mensajes de estado
    @FXML
    public void cerrarAplicacion(){

        System.exit(0);
    }
    @FXML
    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<Usuario> usuario = usuarioRepository.findByNombreAndContraseña(username, password);

        if (usuario.isPresent()) {
            String rol = usuario.get().getRol();
            abrirVentanaSegunRol(rol);
        } else {
            statusLabel.setText("Nombre de usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
    }

    private void abrirVentanaSegunRol(String rol) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            Stage stage = (Stage) loginButton.getScene().getWindow();

            switch (rol) {
                case "Admin":
                    root = cargarFXML("vistaAdmin.fxml");
                    break;
                case "Read":
                    root = cargarFXML("vistaReader.fxml");
                    break;
                case "Write":
                    root = cargarFXML("vistaMeterProyectos.fxml");
                    break;
                default:
                    statusLabel.setText("Rol de usuario no válido.");
                    return;
            }

            if (root != null) {
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                statusLabel.setText("No se pudo cargar la vista.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir la ventana: " + e.getMessage());
        }
    }

    private Parent cargarFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        loader.setControllerFactory(Main.getContext()::getBean); // Usa el contexto de Spring para instanciar el controlador
        return loader.load();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}