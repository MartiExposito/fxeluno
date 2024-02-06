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

/**
 * Controlador para la interfaz de inicio de sesión.
 * Gestiona la autenticación de usuarios y la navegación a diferentes vistas según el rol del usuario.
 */
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

    /**
     * Maneja la acción de inicio de sesión.
     * Autentica al usuario y abre la ventana correspondiente a su rol.
     */
    @FXML
    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<Usuario> usuario = usuarioRepository.findByNombreAndContraseña(username, password);

        if (usuario.isPresent()) {
            String rol = usuario.get().getRol();
            abrirVentanaSegunRol(rol);
        } else {
            mostrarAlerta("Error de Inicio de Sesión", "Nombre de usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
    }

    /**
     * Abre una ventana diferente según el rol del usuario.
     * @param rol el rol del usuario.
     */
    private void abrirVentanaSegunRol(String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/fx/vistaMenu.fxml")); // Asegúrate de que la ruta es correcta
            Parent root = loader.load();

            ControladorVistaMenu controladorMenu = loader.getController();
            controladorMenu.setRol(rol);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Cierra la ventana actual de login
            ((Stage) loginButton.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir el menú: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null); // No header
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    //no es necesario
    }
    /**
     * Cierra la aplicación.
     */
    @FXML
    public void cerrarAplicacion(){

        System.exit(0);
    }
}