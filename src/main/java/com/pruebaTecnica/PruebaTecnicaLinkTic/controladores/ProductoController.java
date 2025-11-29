package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.FondosInsuficientesException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.InventarioInsuficienteException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Clase controller que contiene los endpoints para interactuar con los productos
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoServiceImpl productoServ;

    @Autowired
    private InventarioServiceImpl inventarioServ;

    //EndPoint que permite registrar un producto
    @PostMapping
    public ResponseEntity<?> registrarProducto(@RequestBody Producto nvoProducto){
        //Definimos un inventario por defecto para el producto
        Inventario inventario = new Inventario();
        inventario.setProducto(nvoProducto);
        inventario.setCantidad(100);

        //Agregamos el inventario por defecto y el objeto producto creado para que sea guardado
        return productoServ.saveProducto(nvoProducto, inventario);
    }

    //EndPoint que permite consultar todos los productos registrados en el sistema
    @GetMapping
    public ResponseEntity<?>listarProductos(){
        return ResponseEntity.ok(productoServ.getAllProductos());
    }

    //EndPoint que permite consultar un producto mediante su id
    @GetMapping("/{idProducto}")
    public ResponseEntity<?>  obtenerProducto(@PathVariable Long idProducto){
        Optional<Producto> productoConsultado = productoServ.getProductoById(idProducto);
        //Evaluamos si el producto existe en el sistema
        if(productoConsultado.isEmpty()){
            throw new ExistenciaDeProductoException("El producto no existe.");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(productoConsultado);
        }
    }

    //EndPoint que permite comprar un producto
    @PostMapping("/comprar/{idProducto}/{cantidad}/{dineroRecibido}")
    public ResponseEntity<?>comprarProducto(@PathVariable Long idProducto, @PathVariable Integer cantidad, @PathVariable Integer dineroRecibido){
        //Validamos que el producto exista en el sistema y obtenemos us info importante
        Optional<Producto> productoAComprar = productoServ.getProductoById(idProducto);

        if(productoAComprar.isEmpty()){
            throw new ExistenciaDeProductoException("El producto no existe.");
        }else{
            //Validamos que haya inventario o stock suficiente de dicho producto
            Producto producto = new Producto();
            producto.setId(idProducto);
            Inventario inventarioDeProducto = inventarioServ.obtenerInventarioDeProducto(producto);
            Integer stockDeProducto = inventarioDeProducto.getCantidad();
            if(cantidad <= stockDeProducto){
                //Calculamos cuanto valen los productos y validamos respecto al dinero recibido
                Integer valorAPagar = productoAComprar.get().getPrecio() * cantidad;
                if(dineroRecibido < valorAPagar){
                    throw new FondosInsuficientesException("Fondos insuficientes para la compra.");
                }else{
                    //Descontamos la cantidad de productos solicitada del inventario disponible
                    stockDeProducto = stockDeProducto - cantidad;
                    //Actualizamos el inventario en sistema
                    inventarioDeProducto.setCantidad(stockDeProducto);
                    inventarioServ.actualizarInventario(inventarioDeProducto);
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
            }else{
                throw new InventarioInsuficienteException("La cantidad solicitada supera al inventario disponible.");
            }
        }
    }

}
