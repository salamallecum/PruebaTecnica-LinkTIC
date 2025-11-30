package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.FondosInsuficientesException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.InventarioInsuficienteException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.InventarioRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.ProductoRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public ResponseEntity<?> comprarProducto(Long idProducto, Integer cantidad, Integer dineroRecibido) {
        //Validamos que el producto exista en el sistema y obtenemos us info importante
        Optional<Producto> productoAComprar = productoRepo.findById(idProducto);

        if(productoAComprar.isEmpty()){
            throw new ExistenciaDeProductoException("El producto con id "+idProducto+" no existe.");
        }else {
            //Validamos que haya inventario o stock suficiente de dicho producto
            Producto producto = new Producto();
            producto.setId(idProducto);
            Inventario inventarioDeProducto = inventarioRepo.findByProducto(producto);
            Integer stockDeProducto = inventarioDeProducto.getCantidad();
            if (cantidad <= stockDeProducto) {
                //Calculamos cuanto valen los productos y validamos respecto al dinero recibido
                Integer valorAPagar = productoAComprar.get().getPrecio() * cantidad;
                if (dineroRecibido < valorAPagar) {
                    throw new FondosInsuficientesException("Fondos insuficientes para la compra.");
                } else {
                    //Descontamos la cantidad de productos solicitada del inventario disponible
                    stockDeProducto = stockDeProducto - cantidad;
                    //Actualizamos el inventario en sistema
                    inventarioDeProducto.setCantidad(stockDeProducto);
                    inventarioRepo.save(inventarioDeProducto);
                    //Calculamos el dinero de cambio
                    Integer cambio = valorAPagar - dineroRecibido;
                    //Mapeamos la respuesta
                    Map<String, Object> respuestaServidor = new HashMap<>();
                    respuestaServidor.put("Estado de transacción", "Transacción Exitosa");
                    respuestaServidor.put("Producto", productoAComprar.get().getNombre());
                    respuestaServidor.put("Cantidad", cantidad);
                    respuestaServidor.put("Valor unitario", productoAComprar.get().getPrecio());
                    respuestaServidor.put("Valor a pagar", valorAPagar);
                    respuestaServidor.put("Dinero recibido", dineroRecibido);
                    respuestaServidor.put("Cambio", cambio);
                    return ResponseEntity.ok(respuestaServidor);
                }
            } else {
                throw new InventarioInsuficienteException("La cantidad solicitada supera al inventario disponible.");
            }
        }
    }
}
