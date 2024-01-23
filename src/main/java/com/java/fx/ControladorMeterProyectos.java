package com.java.fx;

import java.io.File;
import java.nio.file.Files;

import com.java.fx.entidades.Documento;
import com.java.fx.entidades.DocumentoRepository;
import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ControladorMeterProyectos {

    private final ProyectoRepository proyectoRepository;

    private final DocumentoRepository documentoRepository;


    @Autowired
    public ControladorMeterProyectos(ProyectoRepository proyectoRepository, DocumentoRepository documentoRepository) {
        this.proyectoRepository = proyectoRepository;
        this.documentoRepository = documentoRepository;
    }

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
    private TableView<Proyecto> tablaProyectos;

    @FXML
    private TableColumn<Proyecto, String> nombreProyectoColumn;

    @FXML
    private TableColumn<Proyecto, LocalDate> fechaCreacionColumn;

    @FXML
    private TableColumn<Proyecto, LocalDate> fechaInicioColumn;

    @FXML
    private TableColumn<Proyecto, LocalDate> fechaFinColumn;

    @FXML
    private TableColumn<Proyecto, String> codigoProyectoColumn;

    @FXML
    private TableColumn<Proyecto, String> palabrasClaveColumn;

    @FXML
    private TableColumn<Proyecto, String> tipoProyectoColumn;

    @FXML
    private TableColumn<Proyecto, Boolean> activoColumn;

    @FXML
    private TableColumn<Proyecto, Integer> calificacionColumn;

    @FXML
    private Button btnVolverLoggin;
    @FXML
    private Button btnSeleccionarArchivos;


    @FXML
    private void guardarProyecto() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombreProyecto(txtNombreProyecto.getText());
        proyecto.setFechaCreacion(dpFechaCreacion.getValue());
        proyecto.setFechaInicio(dpFechaInicio.getValue());
        proyecto.setFechaFin(dpFechaFin.getValue());
        proyecto.setCodigoProyecto(txtCodigoProyecto.getText());
        proyecto.setPalabrasClave(txtPalabrasClave.getText());
        proyecto.setTipoProyecto(txtTipoProyecto.getText());
        proyecto.setActivo(chkActivo.isSelected());
        if (!txtCalificacion.getText().isEmpty()) {
            proyecto.setCalificacion(Integer.parseInt(txtCalificacion.getText()));
        }
        proyecto.setAuditoria(txtAuditoria.getText());
        proyecto.setEnCooperacion(chkEnCooperacion.isSelected());
        proyecto.setBajadaCalificacion(chkBajadaCalificacion.isSelected());
        proyecto.setFases(txtFases.getText());

        proyectoRepository.save(proyecto);

        // Limpia los campos después de guardar
        limpiarCampos();

        // Vuelve a cargar los proyectos en la tabla
        cargarProyectosEnTabla();
    }


    @FXML
    private void seleccionarYGuardarArchivos() {
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado == null) {
            mostrarMensajeError("No se ha seleccionado ningún proyecto.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Documentos");
        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null) {
            for (File file : files) {
                try {
                    byte[] archivo = Files.readAllBytes(file.toPath());
                    Documento documento = new Documento();
                    documento.setArchivo(archivo);
                    documento.setIdProyecto(proyectoSeleccionado); // Asocia el documento al proyecto seleccionado
                    documento.setNombreArchivo(file.getName());


                    documentoRepository.save(documento);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Manejar la excepción adecuadamente
                }
            }
        }
    }

    @FXML
    private void editarProyecto() {
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            try {
                // Utiliza el método cargarFXML para cargar la vista y obtener el controlador
                Parent root = cargarFXML("vistaEditarProyectos.fxml");
                ControladorEditarProyectos dialogController = Main.getContext().getBean(ControladorEditarProyectos.class);
                dialogController.setProyecto(proyectoSeleccionado);

                // Configura y muestra la ventana de diálogo
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Editar Proyecto");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(tablaProyectos.getScene().getWindow());
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();

                // Actualiza la tabla después de cerrar la ventana de diálogo
                cargarProyectosEnTabla();
            } catch (IOException e) {
                mostrarMensajeError("No se pudo cargar la ventana de edición: " + e.getMessage());
            }
        } else {
            mostrarMensajeError("No se ha seleccionado ningún proyecto.");
        }
    }

    @FXML
    private void eliminarProyecto(){
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de que quieres eliminar este proyecto?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.get() == ButtonType.OK) {
                proyectoRepository.delete(proyectoSeleccionado);
                cargarProyectosEnTabla();
            }
        }
    }

    @FXML
    private void irLoggin() throws IOException {

            try {

                Parent root = cargarFXML("main.fxml");
                Stage stage = (Stage) btnVolverLoggin.getScene().getWindow();

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


    @FXML
    private void cancelarProyecto() {

        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombreProyecto.clear();
        dpFechaCreacion.setValue(null);
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
        txtCodigoProyecto.clear();
        txtPalabrasClave.clear();
        txtTipoProyecto.clear();
        chkActivo.setSelected(false);
        txtCalificacion.clear();
        txtAuditoria.clear();
        txtFases.clear();
        chkEnCooperacion.setSelected(false);
        chkBajadaCalificacion.setSelected(false);

    }

    @FXML
    public void initialize() {

        // Configuramos las columnas de la tabla
        nombreProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        fechaFinColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        codigoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        palabrasClaveColumn.setCellValueFactory(new PropertyValueFactory<>("palabrasClave"));
        tipoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));
        activoColumn.setCellValueFactory(new PropertyValueFactory<>("activo"));
        calificacionColumn.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        cargarProyectosEnTabla();

    }
    private void mostrarMensajeError(String mensaje) {
        // Código para mostrar un mensaje de error, por ejemplo, un diálogo de alerta en JavaFX
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void cargarProyectosEnTabla() {
        try {
            tablaProyectos.getItems().clear();
            tablaProyectos.getItems().addAll(proyectoRepository.findAll());
        } catch (Exception e) {
            mostrarMensajeError("Error al cargar proyectos: " + e.getMessage());
        }
    }

}
