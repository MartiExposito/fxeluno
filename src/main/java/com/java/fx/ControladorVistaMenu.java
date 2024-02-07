package com.java.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ControladorVistaMenu {
    @FXML
    Button adminButton;
    @FXML
    Button writerButton;
    @FXML
    Button readerButton;
    @FXML
    Button statsButton;
    @FXML
    Label txtCerrarSesion;

    /**
     * Navega a la vista de gestión de usuarios.
     */
    @FXML
    private void irVistaUsuarios(){
        cargarVista("vistaAdmin.fxml","Gestor Usuarios");
    }
    /**
     * Navega a la vista de edición de proyectos.
     */
    @FXML
    private void irMeterProyetos(){
        cargarVista("vistaMeterProyectos.fxml", "Editor Proyectos");

    }
    /**
     * Navega a la vista de lectura de proyectos.
     */
    @FXML
    private void irVerProyectos(){
        cargarVista("vistaReader.fxml", "Proyectos Reader");
    }
    /**
     * Navega a la vista de estadísticas.
     */
    @FXML
    private void irEstadisticas(){cargarVista("vistaDashboard.fxml", "Dashboard");}
    /**
     * Regresa a la vista de inicio de sesión.
     */
    @FXML
    private void irLoggin(){
        try {

            Parent root = cargarFXML("main.fxml");
            Stage stage = (Stage) txtCerrarSesion.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError(e.getMessage());
        }
    }


    private Parent cargarFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        loader.setControllerFactory(Main.getContext()::getBean); // Usa el contexto de Spring para instanciar el controlador
        return loader.load();
    }
    private void mostrarMensajeError(String mensaje) {
        // Código para mostrar un mensaje de error, por ejemplo, un diálogo de alerta en JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setRol(String rol) {
        configuraBoton(adminButton, rol.equals("Admin"));
        configuraBoton(readerButton, rol.equals("Read") || rol.equals("Admin") || rol.equals("Write"));
        configuraBoton(writerButton, rol.equals("Write") || rol.equals("Admin"));
        configuraBoton(statsButton,  rol.equals("Admin"));
    }

    private void configuraBoton(Button boton, boolean habilitado) {
        boton.setDisable(!habilitado);
        if (habilitado) {
            boton.setStyle("-fx-opacity: 1;"); // Botón habilitado
        } else {
            boton.setStyle("-fx-opacity: 0.5;"); // Botón deshabilitado y semi-transparente
        }
    }
    private void cargarVista(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setControllerFactory(Main.getContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}}
