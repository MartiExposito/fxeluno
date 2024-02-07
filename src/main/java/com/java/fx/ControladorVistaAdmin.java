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
import java.util.ResourceBundle;

/**
 * Controlador para la vista de administrador.
 */
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

    /**
     * Inicializa la vista y configura los elementos iniciales.
     *
     * @param location  La ubicación de la vista.
     * @param resources Los recursos utilizados por la vista.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura las columnas de la tabla de usuarios
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Agrega ejemplos de roles al ComboBox
        comboBoxRoles.getItems().addAll("Admin", "Read", "Write");

        // Carga la lista de usuarios
        cargarUsuarios();
    }

    /**
     * Carga la lista de usuarios en la tabla.
     */
    private void cargarUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarioRepository.findAll()));
    }

    /**
     * Agrega un nuevo usuario a la base de datos con la información proporcionada en la interfaz de usuario.
     */
    @FXML
    private void agregarUsuario() {
        try {
            // Crea un nuevo usuario con los datos de la interfaz
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombreUsuarioTextField.getText());
            nuevoUsuario.setContraseña(contraseñaPasswordField.getText());
            nuevoUsuario.setRol(comboBoxRoles.getValue());

            // Guarda el nuevo usuario en la base de datos
            usuarioRepository.save(nuevoUsuario);

            // Recarga la lista de usuarios y limpia los campos
            cargarUsuarios();
            limpiarCampos();
        } catch (Exception e) {
            // Muestra un mensaje de error en caso de excepción
            mostrarMensajeError("Error al agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Elimina el usuario seleccionado de la base de datos
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
            // Cambia los permisos del usuario seleccionado y guarda en la base de datos
            seleccionado.setRol(comboBoxRoles.getValue());
            usuarioRepository.save(seleccionado);
            cargarUsuarios();
        } else {
            mostrarMensajeError("No se seleccionó ningún usuario.");
        }
    }

    /**
     * Limpia los campos de entrada de datos.
     */
    private void limpiarCampos() {
        nombreUsuarioTextField.clear();
        contraseñaPasswordField.clear();
        comboBoxRoles.getSelectionModel().clearSelection();
    }

    /**
     * Muestra un mensaje de error en una ventana emergente.
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarMensajeError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
