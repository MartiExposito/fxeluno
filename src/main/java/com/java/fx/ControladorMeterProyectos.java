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
import java.util.stream.Collectors;

@Component
public class ControladorMeterProyectos  {

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
    private TextField txtAuditoria;

    @FXML
    private CheckBox chkEnCooperacion;

    @FXML
    private CheckBox chkBajadaCalificacion;

    @FXML
    private ComboBox<String> cbFase;
    @FXML
    private ComboBox<String> cbCalificacion;

    @FXML
    private TableView<Proyecto> tablaProyectos;

    @FXML private TableColumn<Proyecto, Integer> idProyectoColumn;

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
    private TableColumn<Proyecto, String> auditoriaColumn;
    @FXML
    private TableColumn<Proyecto, Boolean> coperacionColumn;
    @FXML
    private TableColumn<Proyecto, Boolean> bajaCalificacionColumn;
    @FXML
    private TableColumn<Proyecto, String> faseColumn;
    @FXML private TextField campoBusqueda;
    @FXML
    private Button btnVolverLoggin;

    @FXML
    private Button btnEditarArchivos;
    @FXML
    Proyecto proyectoActual;

    @FXML
    private void guardarProyecto() {
        if (!validarCampos()) {
            return; // No continúa si la validación falla
        }
        Proyecto nuevoProyecto = new Proyecto();

        nuevoProyecto.setNombreProyecto(txtNombreProyecto.getText());
        nuevoProyecto.setFechaCreacion(dpFechaCreacion.getValue());
        nuevoProyecto.setFechaInicio(dpFechaInicio.getValue());
        nuevoProyecto.setFechaFin(dpFechaFin.getValue());


        nuevoProyecto.setCodigoProyecto(txtCodigoProyecto.getText());
        nuevoProyecto.setPalabrasClave(txtPalabrasClave.getText());
        nuevoProyecto.setTipoProyecto(txtTipoProyecto.getText());
        nuevoProyecto.setActivo(chkActivo.isSelected());
        nuevoProyecto.setAuditoria(txtAuditoria.getText());
        nuevoProyecto.setEnCooperacion(chkEnCooperacion.isSelected());
        nuevoProyecto.setBajadaCalificacion(chkBajadaCalificacion.isSelected());
        String fase = cbFase.getSelectionModel().getSelectedItem();
        String calificacion = cbCalificacion.getSelectionModel().getSelectedItem();
        nuevoProyecto.setFases(fase);
        nuevoProyecto.setCalificacion(calificacion);

        proyectoRepository.save(nuevoProyecto);

        // Limpia los campos después de guardar
        limpiarCampos();
        // Vuelve a cargar los proyectos en la tabla
        cargarProyectosEnTabla();
    }
    private boolean validarCampos() {
        StringBuilder mensajeError = new StringBuilder();

        if (txtNombreProyecto.getText().isEmpty()) {
            mensajeError.append("El nombre del proyecto es requerido.\n");
        }
        if (dpFechaCreacion.getValue() == null) {
            mensajeError.append("La fecha de creación es requerida.\n");
        }
        if (dpFechaInicio.getValue() == null) {
            mensajeError.append("La fecha de inicio es requerida.\n");
        }
        if (dpFechaFin.getValue() == null) {
            mensajeError.append("La fecha de fin es requerida.\n");
        }
        if (txtCodigoProyecto.getText().isEmpty()) {
            mensajeError.append("El código del proyecto es requerido.\n");
        }
        if (txtPalabrasClave.getText().isEmpty()) {
            mensajeError.append("Las palabras clave son requeridas.\n");
        }
        if (txtTipoProyecto.getText().isEmpty()) {
            mensajeError.append("El tipo de proyecto es requerido.\n");
        }
        if (txtAuditoria.getText().isEmpty()) {
            mensajeError.append("La auditoría es requerida.\n");
        }
        if (cbFase.getSelectionModel().isEmpty()) {
            mensajeError.append("La selección de una fase es requerida.\n");
        }
        if (cbCalificacion.getSelectionModel().isEmpty()) {
            mensajeError.append("La selección de una calificación es requerida.\n");
        }

        if (mensajeError.length() != 0) {
            mostrarAlerta("Campos Requeridos", mensajeError.toString());
            return false;
        }

        return true;
    }

    @FXML
    private void filtrarProyectosPorPalabraClave(){
        String textoBusqueda = campoBusqueda.getText().toLowerCase();
        List<Proyecto> proyectosFiltrados = proyectoRepository.findAll().stream()
                .filter(p -> p.getPalabrasClave().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());
        tablaProyectos.getItems().setAll(proyectosFiltrados);

    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
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
    private void editarArchivos() {
        if (!validarCampos()) {
            return; // No continúa si la validación falla
        }
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            try {
                // Cargar la ventana de edición de archivos usando el contexto de Spring
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/fx/vistaEditarArchivos.fxml"));
                loader.setControllerFactory(Main.getContext()::getBean); // Asegúrate de que esto apunta correctamente a tu contexto de Spring
                Parent root = loader.load();

                // Configurar el proyecto seleccionado en el controlador
                ControladorEditarArchivos editarArchivosController = loader.getController();
                editarArchivosController.setProyectoSeleccionado(proyectoSeleccionado);

                // Mostrar la ventana
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarMensajeError("Error al abrir la ventana de edición de archivos.");
            }
        } else {
            mostrarMensajeError("No se ha seleccionado ningún proyecto.");
        }
    }






    @FXML
    private void editarProyecto() {
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            // Cargar los datos del proyecto en los campos de texto y otros controles de la interfaz
            txtNombreProyecto.setText(proyectoSeleccionado.getNombreProyecto());
            dpFechaCreacion.setValue(proyectoSeleccionado.getFechaCreacion());
            dpFechaInicio.setValue(proyectoSeleccionado.getFechaInicio());
            dpFechaFin.setValue(proyectoSeleccionado.getFechaFin());
            txtCodigoProyecto.setText(proyectoSeleccionado.getCodigoProyecto());
            txtPalabrasClave.setText(proyectoSeleccionado.getPalabrasClave());
            txtTipoProyecto.setText(proyectoSeleccionado.getTipoProyecto());
            chkActivo.setSelected(proyectoSeleccionado.getActivo());
          //  txtCalificacion.setText(String.valueOf(proyectoSeleccionado.getCalificacion()));
            txtAuditoria.setText(proyectoSeleccionado.getAuditoria());
            chkEnCooperacion.setSelected(proyectoSeleccionado.getEnCooperacion());
            chkBajadaCalificacion.setSelected(proyectoSeleccionado.getBajadaCalificacion());
           // txtFases.setText(proyectoSeleccionado.getFases());
            cbFase.getSelectionModel().select(proyectoSeleccionado.getFases());
            cbCalificacion.getSelectionModel().select(proyectoSeleccionado.getCalificacion());
            // Guarda una referencia al proyecto actualmente seleccionado
            proyectoActual = proyectoSeleccionado;
        } else {
            mostrarMensajeError("No se ha seleccionado ningún proyecto.");
        }
    }

    @FXML
    private void actualizarProyecto() {
        if (proyectoActual != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de que quieres ACTUALIZAR este proyecto?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.get() == ButtonType.OK) { // Actualiza el proyecto con los datos de los campos de texto y otros controles

                proyectoActual.setNombreProyecto(txtNombreProyecto.getText());
                proyectoActual.setFechaCreacion(dpFechaCreacion.getValue());
                proyectoActual.setFechaInicio(dpFechaInicio.getValue());
                proyectoActual.setFechaFin(dpFechaFin.getValue());
                proyectoActual.setCodigoProyecto(txtCodigoProyecto.getText());
                proyectoActual.setPalabrasClave(txtPalabrasClave.getText());
                proyectoActual.setTipoProyecto(txtTipoProyecto.getText());
                proyectoActual.setActivo(chkActivo.isSelected());

                proyectoActual.setAuditoria(txtAuditoria.getText());
                proyectoActual.setEnCooperacion(chkEnCooperacion.isSelected());
                proyectoActual.setBajadaCalificacion(chkBajadaCalificacion.isSelected());
                //proyectoActual.setFases(txtFases.getText());
                //if (!txtCalificacion.getText().isEmpty()) {
                //proyectoActual.setCalificacion(Integer.parseInt(txtCalificacion.getText())); }
                String faseSeleccionada = cbFase.getSelectionModel().getSelectedItem();
                String calificacionSeleccionada = cbCalificacion.getSelectionModel().getSelectedItem();
                proyectoActual.setFases(faseSeleccionada);
                proyectoActual.setCalificacion(calificacionSeleccionada);
                // Guarda el proyecto actualizado en la base de datos
                proyectoRepository.save(proyectoActual);

                // Limpia los campos y actualiza la tabla
                limpiarCampos();
                cargarProyectosEnTabla();

                // Resetea la referencia al proyecto actual
                proyectoActual = null;
            }} else {
            mostrarMensajeError("No se ha seleccionado ningún proyecto para actualizar.");
        }
    }


    @FXML
    private void eliminarProyecto() {
        Proyecto proyectoSeleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            // Primera confirmación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de que quieres ELIMINAR este PROYECTO?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Segunda confirmación
                Alert confirmacionFinal = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacionFinal.setTitle("Confirmación Final");
                confirmacionFinal.setHeaderText(null);
                confirmacionFinal.setContentText("Esta acción es irreversible. ¿Realmente quieres eliminar el proyecto?");

                Optional<ButtonType> resultadoFinal = confirmacionFinal.showAndWait();
                if (resultadoFinal.isPresent() && resultadoFinal.get() == ButtonType.OK) {
                    proyectoRepository.delete(proyectoSeleccionado);
                    cargarProyectosEnTabla();
                }
            }
        } else {
            mostrarMensajeError("No se ha seleccionado ningún proyecto para eliminar.");
        }
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
        //txtCalificacion.clear();
        txtAuditoria.clear();
        //txtFases.clear();
        chkEnCooperacion.setSelected(false);
        chkBajadaCalificacion.setSelected(false);
        cbFase.getSelectionModel().clearSelection();
        cbFase.setPromptText("Seleccionar Fase");
        cbCalificacion.getSelectionModel().clearSelection();
        cbCalificacion.setPromptText("Seleccionar Calificación");

    }

    @FXML
    public void initialize() {

        // Configuramos las columnas de la tabla
        idProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        fechaFinColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        codigoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        palabrasClaveColumn.setCellValueFactory(new PropertyValueFactory<>("palabrasClave"));
        tipoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));
        activoColumn.setCellValueFactory(new PropertyValueFactory<>("activo"));
        calificacionColumn.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        auditoriaColumn.setCellValueFactory(new PropertyValueFactory<>("auditoria"));
        coperacionColumn.setCellValueFactory(new PropertyValueFactory<>("enCooperacion"));
        bajaCalificacionColumn.setCellValueFactory(new PropertyValueFactory<>("bajadaCalificacion"));
        faseColumn.setCellValueFactory(new PropertyValueFactory<>("fases"));
        cbCalificacion.getItems().addAll("I+D", "IT");
        cbFase.getItems().addAll("1", "2","3","4","5");
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
