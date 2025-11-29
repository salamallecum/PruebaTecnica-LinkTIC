package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

//Clase controller que contiene los endpoints para interactuar con eel inventario
@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    @Autowired
    private InventarioServiceImpl inventarioServ;

    @Autowired
    private ProductoServiceImpl productoServ;

    //EndPoint que retorna la cantidad disponible de un producto teniendo en cuenta su id
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<?> obtenerCantidadDeUnProducto(@PathVariable Long idProducto){
        //Validamos que el producto exista en el sistema
        Optional<Producto> productoConsultado = productoServ.getProductoById(idProducto);
        if(productoConsultado.isEmpty()){
            throw new ExistenciaDeProductoException("El id del producto no existe o no es válido");
        }else {
            Producto producto = new Producto();
            producto.setId(idProducto);
            return ResponseEntity.ok(inventarioServ.obtenerInventarioDeProducto(producto));
        }
    }

    //EndPoint que actualiza el inventario de un producto
    @PutMapping
    public Inventario actualizarInventario(@RequestBody Inventario invenEdit){
        return inventarioServ.actualizarInventario(invenEdit);
    }
}
