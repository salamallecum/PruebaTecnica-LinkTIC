package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.InventarioRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.ProductoRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Clase servicio que implementa los metodos de la interfaz InventarioService
@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepo;

    @Autowired
    private ProductoRepository productoRepo;

    //Metodo que implementa la logica que consulta el inventario de un producto
    @Override
    public Inventario obtenerInventarioDeProducto(Producto producto) {
        return inventarioRepo.findByProducto(producto);
    }

    //Metodo que implementa la logica que actualiza el inventario de un producto
    @Override
    public Inventario actualizarInventario(Inventario inventEdit) {
        return inventarioRepo.save(inventEdit);
    }
}
