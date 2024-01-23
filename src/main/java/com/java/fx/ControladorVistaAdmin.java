package com.java.fx;

import com.java.fx.entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorVistaAdmin implements Initializable {

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private Button agregarUsuarioButton;

    @FXML
    private Button eliminarUsuarioButton;

    @FXML
    private Button cambiarPermisosButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializa tu lógica aquí, si es necesario
    }

    @FXML
    private void agregarUsuario() {
        // Agregar lógica para el botón "Agregar Usuario" aquí
    }

    @FXML
    private void eliminarUsuario() {
        // Agregar lógica para el botón "Eliminar Usuario" aquí
    }

    @FXML
    private void cambiarPermisos() {
        // Agregar lógica para el botón "Cambiar Permisos" aquí
    }
}
