package com.java.fx.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "detallesproyecto")
public class Detallesproyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle", nullable = false)
    private Integer id;

    @Column(name = "auditoria", length = 50)
    private String auditoria;

    @Column(name = "codigo", length = 20)
    private String codigo;

    @Column(name = "en_cooperacion")
    private Boolean enCooperacion;

    @Column(name = "bajada_calificacion")
    private Boolean bajadaCalificacion;

    @Column(name = "fases")
    private String fases;

    public Detallesproyecto(int i, String s, String s1, boolean b, boolean b1, String s2) {
    }

    public Detallesproyecto() {

    }
}