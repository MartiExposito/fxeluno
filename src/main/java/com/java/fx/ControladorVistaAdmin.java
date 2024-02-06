package com.java.fx;

import com.java.fx.entidades.Usuario;
import com.java.fx.entidades.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ControladorVistaAdmin implements Initializable {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, Long> columnaId;
    @FXML
    private TableColumn<Usuario, String> columnaNombre;
    @FXML
    private TableColumn<Usuario, String> columnaRol;

    @FXML
    private TextField nombreUsuarioTextField;
    @FXML
    private PasswordField contraseñaPasswordField;
    @FXML
    private ComboBox<String> comboBoxRoles;

    @FXML
    private Button agregarUsuarioButton;
    @FXML
    private Button eliminarUsuarioButton;
    @FXML
    private Button cambiarPermisosButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        comboBoxRoles.getItems().addAll("Admin", "Read", "Write"); // Ejemplo de roles
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarioRepository.findAll()));
    }
    /**
     * Agrega un nuevo usuario a la base de datos con la información proporcionada en la interfaz de usuario.
     */
    @FXML
    private void agregarUsuario() {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombreUsuarioTextField.getText());
            nuevoUsuario.setContraseña(contraseñaPasswordField.getText());
            nuevoUsuario.setRol(comboBoxRoles.getValue());

            usuarioRepository.save(nuevoUsuario);
            cargarUsuarios();
            limpiarCampos();
        } catch (Exception e) {
            mostrarMensajeError("Error al agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            usuarioRepository.delete(seleccionado);
            cargarUsuarios();
        } else {
            mostrarMensajeError("No se seleccionó ningún usuario.");
        }
    }

    @FXML
    private void cambiarPermisos() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.setRol(comboBoxRoles.getValue());
            usuarioRepository.save(seleccionado);
            cargarUsuarios();
        } else {
            mostrarMensajeError("No se seleccionó ningún usuario.");
        }
    }

    private void limpiarCampos() {
        nombreUsuarioTextField.clear();
        contraseñaPasswordField.clear();
        comboBoxRoles.getSelectionModel().clearSelection();
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
