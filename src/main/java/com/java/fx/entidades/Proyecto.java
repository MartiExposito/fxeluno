package com.java.fx.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto", nullable = false)
    private Integer id;

    @Column(name = "nombre_proyecto", nullable = false)
    private String nombreProyecto;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "codigo_proyecto", nullable = false, length = 20)
    private String codigoProyecto;

    @Column(name = "palabras_clave")
    private String palabrasClave;

    @Column(name = "tipo_proyecto", length = 50)
    private String tipoProyecto;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "calificacion")
    private Integer calificacion;

    @Column(name = "auditoria", length = 50)
    private String auditoria;

    @Column(name = "en_cooperacion")
    private Boolean enCooperacion;

    @Column(name = "bajada_calificacion")
    private Boolean bajadaCalificacion;

    @Column(name = "fases")
    private String fases;

    public Proyecto(int i, String s, LocalDate now, String s1, String s2, String s3) {
    }

    public Proyecto() {

    }
}