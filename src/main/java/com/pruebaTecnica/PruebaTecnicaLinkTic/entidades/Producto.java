package com.pruebaTecnica.PruebaTecnicaLinkTic.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "productos")
//Clase que modela el objeto de tipo Producto
public class Producto {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private Integer precio;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    private Inventario inventario;

}
