package com.java.fx;

import com.java.fx.entidades.Documento;
import com.java.fx.entidades.DocumentoRepository;
import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ControladorEditarArchivos {

    private Proyecto proyectoSeleccionado;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;




    @FXML
    private TableView<Proyecto> tablaProyectos;

    @FXML
    private TableView<Documento> tablaDocumentos;

    @FXML
    private TableColumn<Proyecto, String> nombreProyectoColumn;
    @FXML
    private TableColumn<Proyecto, Integer> idProyectoColumn;
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
    private TableColumn<Proyecto, Boolean> enCooperacionColumn;
    @FXML
    private TableColumn<Proyecto, Boolean> bajadaCalificacionColumn;
    @FXML
    private TableColumn<Proyecto, String> fasesColumn;

    @FXML
    private TableColumn<Documento, Integer> idDocumentoColumn;
    @FXML
    private TableColumn<Documento, Integer> idProyectoDocumentoColumn;
    @FXML
    private TableColumn<Documento, String> nombreArchivoColumn;
    @FXML
    TextField campoBusqueda;
    public ControladorEditarArchivos() {

    }

    @FXML
    private void filtrarProyectosPorPalabraClave(){
        String textoBusqueda = campoBusqueda.getText().toLowerCase();
        List<Proyecto> proyectosFiltrados = proyectoRepository.findAll().stream()
                .filter(p -> p.getPalabrasClave().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());
        tablaProyectos.getItems().setAll(proyectosFiltrados);

    }
    public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
        cargarDocumentosDelProyecto();
    }


    private void cargarDocumentosDelProyecto() {
        if (proyectoSeleccionado != null) {
            List<Documento> documentos = documentoRepository.findByIdProyecto(proyectoSeleccionado);
            tablaDocumentos.getItems().clear();
            tablaDocumentos.getItems().addAll(documentos);
        }
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        configurarColumnasProyecto();
        configurarColumnasDocumento();
        cargarProyectos();
        escucharSeleccionProyecto();
    }

    private void configurarColumnasProyecto() {
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
        enCooperacionColumn.setCellValueFactory(new PropertyValueFactory<>("enCooperacion"));
        bajadaCalificacionColumn.setCellValueFactory(new PropertyValueFactory<>("bajadaCalificacion"));
        fasesColumn.setCellValueFactory(new PropertyValueFactory<>("fases"));
    }

    private void configurarColumnasDocumento() {
        idDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idProyectoDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("idProyecto"));
        nombreArchivoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));

    }

    private void cargarProyectos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        tablaProyectos.getItems().setAll(proyectos);
    }

    private void escucharSeleccionProyecto() {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setProyectoSeleccionado(newSelection);
            }
        });
    }

    @FXML
    private void actionAñadirDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Documento");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                byte[] archivo = Files.readAllBytes(file.toPath());
                Documento documento = new Documento();
                documento.setArchivo(archivo);
                documento.setIdProyecto(proyectoSeleccionado);
                documento.setNombreArchivo(file.getName());

                documentoRepository.save(documento);
                cargarDocumentosDelProyecto();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarMensajeError("Error al agregar el documento.");
            }
        }
    }
    @FXML
    private void actionEliminarDoc() {
        Documento documentoSeleccionado = tablaDocumentos.getSelectionModel().getSelectedItem();
        if (documentoSeleccionado != null) {

            // Primera confirmación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de que quieres ELIMINAR este DOCUMENTO?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Segunda confirmación
                Alert confirmacionFinal = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacionFinal.setTitle("Confirmación Final");
                confirmacionFinal.setHeaderText(null);
                confirmacionFinal.setContentText("Esta acción es irreversible. ¿Realmente quieres eliminar el documento?");

                Optional<ButtonType> resultadoFinal = confirmacionFinal.showAndWait();
                if (resultadoFinal.isPresent() && resultadoFinal.get() == ButtonType.OK) {
                    documentoRepository.delete(documentoSeleccionado);
                    cargarDocumentosDelProyecto();
                }
            }
        } else {
            mostrarMensajeError("No se ha seleccionado ningún documento para eliminar.");
        }
    }



}
