package com.java.fx;

import com.java.fx.entidades.Documento;
import com.java.fx.entidades.DocumentoRepository;
import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ControladorVistaReader {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;
    /**
     * Constructor del controlador que recibe los repositorios como parámetros.
     * @param proyectoRepository Repositorio de proyectos.
     * @param documentoRepository Repositorio de documentos.
     */
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

    @FXML private TextField campoBusqueda;

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
        configurarDobleClicEnDocumentos();
    }

    /**
     * Filtra los proyectos por palabras clave ingresadas en el campo de búsqueda.
     */
    @FXML
    private void filtrarProyectosPorPalabraClave(){
        String textoBusqueda = campoBusqueda.getText().toLowerCase();
        List<Proyecto> proyectosFiltrados = proyectoRepository.findAll().stream()
                .filter(p -> p.getPalabrasClave().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());
        tablaProyectos.getItems().setAll(proyectosFiltrados);

    }
    /**
     * Configura las columnas de la tabla de proyectos.
     */
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
    /**
     * Configura las columnas de la tabla de documentos.
     */
    private void configurarColumnasDocumento() {

        idProyectoDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("idProyecto"));
        archivoDocumentoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));

    }

    /**
     * Carga la lista de proyectos en la tabla de proyectos.
     */
    private void cargarProyectos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        tablaProyectos.getItems().setAll(proyectos);
    }
    /**
     * Carga la lista de documentos correspondientes al proyecto seleccionado.
     */
    private void cargarDocumentos(Proyecto proyecto) {
        List<Documento> documentos = documentoRepository.findByIdProyecto(proyecto);
        tablaDocumentos.getItems().setAll(documentos);
    }

    /**
     * Escucha la selección de un proyecto en la tabla y carga los documentos correspondientes.
     */
    private void escucharSeleccionProyecto() {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDocumentos(newSelection);
            }
        });
    }
    /**
     * Configura el evento de doble clic en documentos para abrirlos.
     */
    private void configurarDobleClicEnDocumentos() {
        tablaDocumentos.setRowFactory(tv -> {
            TableRow<Documento> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Documento documentoSeleccionado = row.getItem();
                    abrirDocumento(documentoSeleccionado);
                }
            });
            return row;
        });
    }
    /**
     * Abre el documento seleccionado en una ventana externa o lo descarga.
     * @param documento Documento a abrir o descargar.
     */
    private void abrirDocumento(Documento documento) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(documento.getNombreArchivo());

        File archivoDestino = fileChooser.showSaveDialog(tablaDocumentos.getScene().getWindow());
        if (archivoDestino != null) {
            guardarYAbrirArchivo(documento, archivoDestino);
        }
    }
    /**
     * Guarda el archivo en disco y lo abre en la aplicación predeterminada.
     * @param documento Documento a guardar y abrir.
     * @param archivoDestino Ruta del archivo de destino.
     */
    private void guardarYAbrirArchivo(Documento documento, File archivoDestino) {
        try {
            byte[] contenidoArchivo = documento.getArchivo();
            try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                fos.write(contenidoArchivo);
            }

            Desktop.getDesktop().open(archivoDestino);
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error adecuadamente
        }
    }
}