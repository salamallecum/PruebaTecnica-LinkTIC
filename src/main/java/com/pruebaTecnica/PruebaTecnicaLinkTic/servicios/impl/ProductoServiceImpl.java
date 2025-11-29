package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.InventarioRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.ProductoRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Clase servicio que implementa los metodos de la interfaz ProductoService
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private InventarioRepository inventarioRepo;

    //Metodo que implementa la logica de registro de un producto
    @Override
    public ResponseEntity<?> saveProducto(Producto nvoProducto, Inventario nvoInventario) {
        Optional<Producto> productoGuardado = productoRepo.findByNombre(nvoProducto.getNombre());
        //Validamos si ya existe un producto con ese nombre, si no, lo guardamos
        if(productoGuardado.isPresent()){
            throw new ExistenciaDeProductoException("Ya existe un producto con ese nombre: "+ nvoProducto.getNombre());
        }else{
            //Guardamos el producto con su inventario correspondiente
            inventarioRepo.save(nvoInventario);
            productoRepo.save(nvoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nvoProducto);
        }
    }

    //Metodo que implementa la logica de consulta de todos los productos
    @Override
    public List<Producto> getAllProductos() {
        return productoRepo.findAll();
    }

    //Metodo que implementa la logica de consulta de un producto por su id
    @Override
    public Optional<Producto> getProductoById(long id) {
        return productoRepo.findById(id);
    }
}
