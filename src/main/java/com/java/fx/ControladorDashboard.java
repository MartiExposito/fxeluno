package com.java.fx;

import com.java.fx.entidades.Proyecto;
import com.java.fx.entidades.ProyectoRepository;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ControladorDashboard {

    // Etiquetas para mostrar estadísticas
    @FXML
    private Label labelTotalProyectos;
    @FXML
    private Label labelProyectosActivos;
    @FXML
    private Label labelProyectosInactivos;

    // Gráficos y botón de actualización
    @FXML
    private PieChart pieChartActivosInactivos;
    @FXML
    private PieChart pieChartProyectosPorFase;
    @FXML
    private BarChart<String, Number> barChartMediaCalificaciones;
    @FXML
    private BarChart<String, Number> barChartPorTipoProyecto;
    @FXML
    private LineChart<String, Number> lineChartTendenciasTemporales;
    @FXML
    private Button btnActualizar;

    // Repositorio para acceder a los datos de los proyectos
    @Autowired
    private ProyectoRepository proyectoRepository;

    // Método que se ejecuta al cargar el controlador
    @FXML
    public void initialize() {
        cargarEstadisticas();
    }

    // Carga y muestra las estadísticas
    private void cargarEstadisticas() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        labelTotalProyectos.setText(String.valueOf(proyectos.size()));

        long proyectosActivos = proyectos.stream().filter(Proyecto::getActivo).count();
        labelProyectosActivos.setText(String.valueOf(proyectosActivos));
        labelProyectosInactivos.setText(String.valueOf(proyectos.size() - proyectosActivos));

        actualizarPieChartActivosInactivos(proyectos);
        actualizarPieChartProyectosPorFase(proyectos);
        actualizarBarChartMediaCalificaciones(proyectos);
        actualizarBarChartPorTipoProyecto(proyectos);
        actualizarLineChartTendenciasTemporales(proyectos);
    }

    // Actualiza el gráfico de proyectos activos e inactivos
    private void actualizarPieChartActivosInactivos(List<Proyecto> proyectos) {
        long activos = proyectos.stream().filter(Proyecto::getActivo).count();
        long inactivos = proyectos.size() - activos;

        PieChart.Data slice1 = new PieChart.Data("Activos (" + activos + ")", activos);
        PieChart.Data slice2 = new PieChart.Data("Inactivos (" + inactivos + ")", inactivos);

        pieChartActivosInactivos.getData().clear();
        pieChartActivosInactivos.getData().addAll(slice1, slice2);
    }

    // Actualiza el gráfico de proyectos por fase
    private void actualizarPieChartProyectosPorFase(List<Proyecto> proyectos) {
        Map<String, Long> proyectosPorFase = proyectos.stream()
                .collect(Collectors.groupingBy(Proyecto::getFases, Collectors.counting()));

        pieChartProyectosPorFase.getData().clear();
        for (Map.Entry<String, Long> entry : proyectosPorFase.entrySet()) {
            String fase = entry.getKey();
            Long cantidad = entry.getValue();
            PieChart.Data slice = new PieChart.Data(fase + " (" + cantidad + ")", cantidad);
            pieChartProyectosPorFase.getData().add(slice);
        }
    }

    // Actualiza el gráfico de media de calificaciones
    private void actualizarBarChartMediaCalificaciones(List<Proyecto> proyectos) {
        Map<String, Long> proyectosPorCalificacion = proyectos.stream()
                .collect(Collectors.groupingBy(Proyecto::getCalificacion, Collectors.counting()));

        barChartMediaCalificaciones.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        proyectosPorCalificacion.forEach((calificacion, cantidad) ->
                series.getData().add(new XYChart.Data<>(calificacion, cantidad)));
        barChartMediaCalificaciones.getData().add(series);
    }

    // Actualiza el gráfico de proyectos por tipo
    private void actualizarBarChartPorTipoProyecto(List<Proyecto> proyectos) {
        Map<String, Long> proyectosPorTipo = proyectos.stream()
                .collect(Collectors.groupingBy(Proyecto::getTipoProyecto, Collectors.counting()));

        barChartPorTipoProyecto.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        proyectosPorTipo.forEach((tipo, cantidad) ->
                series.getData().add(new XYChart.Data<>(tipo, cantidad)));
        barChartPorTipoProyecto.getData().add(series);
    }

    // Método para actualizar los gráficos cuando se hace clic en el botón "Actualizar"
    @FXML
    private void actualizarGraficos() {
        cargarEstadisticas();
    }

    // Actualiza el gráfico de tendencias temporales
    private void actualizarLineChartTendenciasTemporales(List<Proyecto> proyectos) {
        LocalDate hoy = LocalDate.now();

        Map<YearMonth, Long> proyectosPorMes = proyectos.stream()
                .filter(proyecto -> proyecto.getFechaFin() != null && proyecto.getFechaFin().isBefore(hoy))
                .collect(Collectors.groupingBy(proyecto -> YearMonth.from(proyecto.getFechaFin()),
                        TreeMap::new,
                        Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        proyectosPorMes.forEach((yearMonth, count) -> {
            String label = yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + "-" + yearMonth.getYear();
            series.getData().add(new XYChart.Data<>(label, count));
        });

        lineChartTendenciasTemporales.getData().clear();
        lineChartTendenciasTemporales.getData().add(series);
        lineChartTendenciasTemporales.getXAxis().setLabel("Fecha");
        lineChartTendenciasTemporales.getYAxis().setLabel("Proyectos Finalizados");
    }
}
