package com.java.fx;

import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
@Component
public class ControladorEditarProyectos {

    @FXML
    private TextField txtNombreProyecto;

    @FXML
    private DatePicker dpFechaCreacion;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private TextField txtCodigoProyecto;

    @FXML
    private TextField txtPalabrasClave;

    @FXML
    private TextField txtTipoProyecto;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private TextField txtCalificacion;

    @FXML
    private TextField txtAuditoria;

    @FXML
    private CheckBox chkEnCooperacion;

    @FXML
    private CheckBox chkBajadaCalificacion;

    @FXML
    private TextField txtFases;
    @FXML
    private Button btnVolverLoggin;

    private Proyecto proyectoActual;

    @Autowired
    private ProyectoRepository proyectoRepository;

    public void setProyecto(Proyecto proyectoSeleccionado) {
        this.proyectoActual = proyectoSeleccionado;
        cargarDatosProyecto();
    }

    private void cargarDatosProyecto() {

        txtNombreProyecto.setText(proyectoActual.getNombreProyecto());
        dpFechaCreacion.setValue(proyectoActual.getFechaCreacion());
        dpFechaInicio.setValue(proyectoActual.getFechaInicio());
        dpFechaFin.setValue(proyectoActual.getFechaFin());
        txtCodigoProyecto.setText(proyectoActual.getCodigoProyecto());
        txtPalabrasClave.setText(proyectoActual.getPalabrasClave());
        txtTipoProyecto.setText(proyectoActual.getTipoProyecto());
        chkActivo.setSelected(proyectoActual.getActivo());
        txtCalificacion.setText(proyectoActual.getCalificacion().toString());
        txtAuditoria.setText(proyectoActual.getAuditoria());
        chkEnCooperacion.setSelected(proyectoActual.getEnCooperacion() != null ? proyectoActual.getEnCooperacion() : false);
        chkBajadaCalificacion.setSelected(proyectoActual.getBajadaCalificacion() != null ? proyectoActual.getBajadaCalificacion() : false);
        txtFases.setText(proyectoActual.getFases());

    }
    private void mostrarMensajeError(String mensaje) {
        // Código para mostrar un mensaje de error, por ejemplo, un diálogo de alerta en JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void guardarProyecto() {
        // Asegúrate de que `proyectoActual` no sea null
        if (proyectoActual != null) {
            // Toma los datos de los campos de la interfaz y los asigna al objeto proyectoActual
            proyectoActual.setNombreProyecto(txtNombreProyecto.getText());
            proyectoActual.setFechaCreacion(dpFechaCreacion.getValue());
            proyectoActual.setFechaInicio(dpFechaInicio.getValue());
            proyectoActual.setFechaFin(dpFechaFin.getValue());
            proyectoActual.setCodigoProyecto(txtCodigoProyecto.getText());
            proyectoActual.setPalabrasClave(txtPalabrasClave.getText());
            proyectoActual.setTipoProyecto(txtTipoProyecto.getText());
            proyectoActual.setActivo(chkActivo.isSelected());
            proyectoActual.setCalificacion(Integer.parseInt(txtCalificacion.getText()));
            proyectoActual.setAuditoria(txtAuditoria.getText());
            proyectoActual.setEnCooperacion(chkEnCooperacion.isSelected());
            proyectoActual.setBajadaCalificacion(chkBajadaCalificacion.isSelected());
            proyectoActual.setFases(txtFases.getText());

            // Llama al método save del repositorio para actualizar los datos en la base de datos
            proyectoRepository.save(proyectoActual);

            // Cierra la ventana de diálogo
            ((Stage) txtNombreProyecto.getScene().getWindow()).close();
        } else {
            mostrarMensajeError("No se ha cargado ningún proyecto para editar.");
        }
    }

    @FXML
    private void irLoggin() throws IOException {
       try {
           FXMLLoader loader = new FXMLLoader();
           Parent root = null;
           Stage stage = (Stage) btnVolverLoggin.getScene().getWindow();
       }catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError(e.getMessage());
        }
    }

}
