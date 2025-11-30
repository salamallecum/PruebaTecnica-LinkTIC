package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.CompraResponse;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.ErrorResponse;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.FondosInsuficientesException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.InventarioInsuficienteException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//Clase controller que contiene los endpoints para interactuar con los productos
@Tag(name = "Productos", description = "EndPoints que permiten la gestión de los productos")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoServiceImpl productoServ;

    @Autowired
    private InventarioServiceImpl inventarioServ;

    //EndPoint que permite registrar un producto
    @Operation(summary = "Registrar producto", description = "Registra un producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna estado http 201 con la información del producto registrado.", content = @Content(schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "409", description = "Retorna estado http 409 a manera de excepción cuando encuentra un producto registrado en el sistema con el mismo nombre al suministrado por el producto a registrar.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"Ya existe un producto con ese nombre: Arroz.\",\"timestamp\":\"2025-11-29T10:41:36.27495\"}")))
    })
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
    @Operation(summary = "Consultar productos", description = "Obtiene el listado de productos que han sido registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna estado http 200 con el listado de productos.", content = @Content(schema = @Schema(implementation = Producto.class)))
    })
    @GetMapping
    public ResponseEntity<?>listarProductos(){
        return ResponseEntity.ok(productoServ.getAllProductos());
    }

    //EndPoint que permite consultar un producto mediante su id
    @Operation(summary = "Consultar producto", description = "Obtiene un producto teniendo en cuenta su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna estado http 200 con la información registrada del producto.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "409", description = "Retorna estado http 409 cuando el id del producto no sea válido o el producto no exista.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"El producto con id 123 no existe\",\"timestamp\":\"2025-11-29T10:41:36.27495\"}")))
    })
    @GetMapping("/{idProducto}")
    public ResponseEntity<?>  obtenerProducto(@PathVariable Long idProducto){
        Optional<Producto> productoConsultado = productoServ.getProductoById(idProducto);
        //Evaluamos si el producto existe en el sistema
        if(productoConsultado.isEmpty()){
            throw new ExistenciaDeProductoException("El producto con id "+idProducto+" no existe.");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(productoConsultado);
        }
    }

    //EndPoint que permite comprar un producto
    @Operation(summary = "Comprar producto", description = "Lleva a cabo el proceso de compra de un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna estado http 200 junto con una respuesta tipo JSON en donde se especifica el estado exitoso de la transacción, el nombre del producto, cantidad, valor unitario, valor a pagar, dinero recibido y dinero de vueltas o cambio.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompraResponse.class),
                            examples = @ExampleObject(value = "{\"estadoDeTransaccion\":\"Transacción exitosa.\",\"nombreProducto\":\"Arroz\",\"cantidad\":2,\"valorUnitario\":20000,\"valorAPagar\":40000,\"dineroRecibido\":41000,\"cambio\":1000}"))),
            @ApiResponse(responseCode = "409", description = "Retorna estado hhtp 409 en caso de que el producto no exista, el dinero recibido sea insuficiente o la cantidad de producto solicitada supere al inventario disponible.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"Fondos insuficientes para la compra.\",\"timestamp\":\"2025-11-29T10:41:36.27495\"}")))
    })


    @PostMapping("/comprar/{idProducto}/{cantidad}/{dineroRecibido}")
    public ResponseEntity<?>comprarProducto(@PathVariable Long idProducto, @PathVariable Integer cantidad, @PathVariable Integer dineroRecibido){
        return productoServ.comprarProducto(idProducto, cantidad, dineroRecibido);
    }
}
