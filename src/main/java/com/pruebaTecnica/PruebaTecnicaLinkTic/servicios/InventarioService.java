package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

//Interfaz servicio que define los metodos de un inventario
public interface InventarioService {

    //Metodo que permite consultar el inventario de un producto teniendo en cuenta su id
    Inventario obtenerInventarioDeProducto(Producto producto);

    //Metodo que permite actualizar la cantidad disponible de un producto
    Inventario actualizarInventario(Inventario inventEdit);

}
