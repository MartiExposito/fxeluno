package com.java.fx;

import com.java.fx.entidades.Detallesproyecto;
import com.java.fx.entidades.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControladorVistaReader implements Initializable {

    @FXML
    private TableView<Detallesproyecto> tablaDetallesProyecto;
    @FXML
    private TableColumn<Detallesproyecto, Integer> idDetallesProyectoColumn;
    @FXML
    private TableColumn<Detallesproyecto, String> auditoriaColumn;
    @FXML
    private TableColumn<Detallesproyecto, String> codigoColumn;
    @FXML
    private TableColumn<Detallesproyecto, Boolean> enCooperacionColumn;
    @FXML
    private TableColumn<Detallesproyecto, Boolean> bajadaCalificacionColumn;
    @FXML
    private TableColumn<Detallesproyecto, String> fasesColumn;

    @FXML
    private TableView<Proyecto> tablaProyectos;
    @FXML
    private TableColumn<Proyecto, Integer> idProyectoColumn;
    @FXML
    private TableColumn<Proyecto, String> nombreProyectoColumn;
    @FXML
    private TableColumn<Proyecto, LocalDate> fechaCreacionColumn;
    @FXML
    private TableColumn<Proyecto, String> codigoProyectoColumn;
    @FXML
    private TableColumn<Proyecto, String> palabrasClaveColumn;
    @FXML
    private TableColumn<Proyecto, String> tipoProyectoColumn;

    @FXML
    private TextField filtroAuditoria;
    @FXML
    private TextField filtroCodigo;
    @FXML
    private TextField filtroNombreProyecto;
    @FXML
    private TextField filtroCodigoProyecto;

    private ObservableList<Detallesproyecto> detallesProyectoData;
    private ObservableList<Proyecto> proyectosData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializa las columnas de Detallesproyecto
        idDetallesProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        auditoriaColumn.setCellValueFactory(new PropertyValueFactory<>("auditoria"));
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        enCooperacionColumn.setCellValueFactory(new PropertyValueFactory<>("enCooperacion"));
        bajadaCalificacionColumn.setCellValueFactory(new PropertyValueFactory<>("bajadaCalificacion"));
        fasesColumn.setCellValueFactory(new PropertyValueFactory<>("fases"));

        // Inicializa las columnas de Proyecto
        idProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        codigoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        palabrasClaveColumn.setCellValueFactory(new PropertyValueFactory<>("palabrasClave"));
        tipoProyectoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));

        // Llena las tablas con datos de ejemplo (reemplaza esto con tus datos reales)
        detallesProyectoData = FXCollections.observableArrayList(getDetallesProyectoData());
        tablaDetallesProyecto.setItems(detallesProyectoData);

        proyectosData = FXCollections.observableArrayList(getProyectosData());
        tablaProyectos.setItems(proyectosData);

        // Configura los filtros para las tablas
        configurarFiltros();
    }

    // Aquí debes implementar métodos para obtener tus datos reales de Detallesproyecto y Proyecto

    private List<Detallesproyecto> getDetallesProyectoData() {
        // Aquí debes implementar la lógica para obtener los datos de Detallesproyecto
        // Por ahora, se retornan datos de ejemplo
        return List.of(
                new Detallesproyecto(1, "Auditoria 1", "Codigo 1", true, false, "Fases 1"),
                new Detallesproyecto(2, "Auditoria 2", "Codigo 2", false, true, "Fases 2")
        );
    }

    private List<Proyecto> getProyectosData() {
        // Aquí debes implementar la lógica para obtener los datos de Proyecto
        // Por ahora, se retornan datos de ejemplo
        return List.of(
                new Proyecto(1, "Proyecto 1", LocalDate.now(), "Código 1", "Palabras clave 1", "Tipo 1"),
                new Proyecto(2, "Proyecto 2", LocalDate.now(), "Código 2", "Palabras clave 2", "Tipo 2")
        );
    }

    private void configurarFiltros() {
        // Filtrar Detallesproyecto por auditoria y código
        filtroAuditoria.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarDetallesProyecto();
        });

        filtroCodigo.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarDetallesProyecto();
        });

        // Filtrar Proyecto por nombre de proyecto y código de proyecto
        filtroNombreProyecto.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProyectos();
        });

        filtroCodigoProyecto.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProyectos();
        });
    }

    private void filtrarDetallesProyecto() {
        // Filtrar los datos de Detallesproyecto según los filtros de auditoria y código
        List<Detallesproyecto> filteredData = getDetallesProyectoData().stream()
                .filter(detalle -> detalle.getAuditoria().toLowerCase().contains(filtroAuditoria.getText().toLowerCase()))
                .filter(detalle -> detalle.getCodigo().toLowerCase().contains(filtroCodigo.getText().toLowerCase()))
                .collect(Collectors.toList());

        detallesProyectoData.setAll(filteredData);
    }

    private void filtrarProyectos() {
        // Filtrar los datos de Proyecto según los filtros de nombre de proyecto y código de proyecto
        List<Proyecto> filteredData = getProyectosData().stream()
                .filter(proyecto -> proyecto.getNombreProyecto().toLowerCase().contains(filtroNombreProyecto.getText().toLowerCase()))
                .filter(proyecto -> proyecto.getCodigoProyecto().toLowerCase().contains(filtroCodigoProyecto.getText().toLowerCase()))
                .collect(Collectors.toList());

        proyectosData.setAll(filteredData);
    }
}
