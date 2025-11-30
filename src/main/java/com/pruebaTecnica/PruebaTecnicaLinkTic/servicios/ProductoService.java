package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//Interfaz de servicio que define los metodos de un producto
public interface ProductoService {

    //Metodo que permite crear un producto
    ResponseEntity<?> saveProducto(Producto nvoProducto, Inventario nvoInventario);

    //Metodo que lista todos los productos
    List<Producto> getAllProductos();

    //Metodo que retorna la info de un producto por su id
    Optional<Producto> getProductoById(long id);

    //Metodo que permite comprar un producto
    ResponseEntity<?> comprarProducto(Long idProducto, Integer cantidad, Integer dineroRecibido);
}
