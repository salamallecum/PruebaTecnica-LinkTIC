package com.pruebaTecnica.PruebaTecnicaLinkTic.entidades;

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
@Table(name = "inventarios")
//Clase que modela el objeto de tipo Inventario
public class Inventario {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;

}
