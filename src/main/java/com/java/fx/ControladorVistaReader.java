package com.java.fx;

import com.java.fx.entidades.Documento;
import com.java.fx.entidades.DocumentoRepository;
import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Component
public class ControladorVistaReader {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    public ControladorVistaReader(ProyectoRepository proyectoRepository, DocumentoRepository documentoRepository) {
        this.proyectoRepository = proyectoRepository;
        this.documentoRepository = documentoRepository;
    }
    @FXML
    private TableView<Proyecto> tablaProyectos;
    @FXML
    private TableView<Documento> tablaDocumentos;

    @FXML
    private TableColumn<Proyecto, String> nombreProyectoColumn;
    // Columnas para la entidad Proyecto
    @FXML private TableColumn<Proyecto, Integer> idProyectoColumn;
    @FXML private TableColumn<Proyecto, LocalDate> fechaCreacionColumn;
    @FXML private TableColumn<Proyecto, LocalDate> fechaInicioColumn;
    @FXML private TableColumn<Proyecto, LocalDate> fechaFinColumn;
    @FXML private TableColumn<Proyecto, String> codigoProyectoColumn;
    @FXML private TableColumn<Proyecto, String> palabrasClaveColumn;
    @FXML private TableColumn<Proyecto, String> tipoProyectoColumn;
    @FXML private TableColumn<Proyecto, Boolean> activoColumn;
    @FXML private TableColumn<Proyecto, Integer> calificacionColumn;
    @FXML private TableColumn<Proyecto, String> auditoriaColumn;
    @FXML private TableColumn<Proyecto, Boolean> enCooperacionColumn;
    @FXML private TableColumn<Proyecto, Boolean> bajadaCalificacionColumn;
    @FXML private TableColumn<Proyecto, String> fasesColumn;


    @FXML
    private TableColumn<Documento, Integer> idDocumentoColumn;
    // Columnas para la entidad Documento
    @FXML private TableColumn<Documento, Integer> idProyectoDocumentoColumn;
    @FXML private TableColumn<Documento, byte[]> archivoDocumentoColumn;


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
        // Asumiendo que idProyectoDocumentoColumn muestra el ID del proyecto asociado al documento
        idProyectoDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("idProyecto"));
        archivoDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        // Si necesitas añadir más columnas para la entidad Documento, hazlo aquí
    }


    private void cargarProyectos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        tablaProyectos.getItems().setAll(proyectos);
    }

    private void cargarDocumentos(Proyecto proyecto) {
        List<Documento> documentos = documentoRepository.findByIdProyecto(proyecto);
        tablaDocumentos.getItems().setAll(documentos);
    }

    private void escucharSeleccionProyecto() {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDocumentos(newSelection);
            }
        });
    }
}
