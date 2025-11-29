package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.ErrorResponse;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


//Clase controller que contiene los endpoints para interactuar con eel inventario
@Tag(name = "Inventarios", description = "EndPoints que permiten la gestión de los inventarios")
@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    @Autowired
    private InventarioServiceImpl inventarioServ;

    @Autowired
    private ProductoServiceImpl productoServ;

    //EndPoint que retorna el inventario de un producto teniendo en cuenta su id
    @Operation(summary = "Consultar inventario", description = "EndPoint que retorna el inventario de un producto teniendo en cuenta su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna estado http 200 con la info del inventario asociado al id de un producto dado.", content = @Content(schema = @Schema(implementation = Inventario.class))),
            @ApiResponse(responseCode = "409", description = "Retorna estado http 409 a manera de excepción indicando que el id del producto no existe o no es válido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"El id del producto 255 no existe o no es válido\",\"timestamp\":\"2025-11-29T10:41:36.27495\"}")))
    })
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<?> obtenerCantidadDeUnProducto(@PathVariable Long idProducto){
        //Validamos que el producto exista en el sistema
        Optional<Producto> productoConsultado = productoServ.getProductoById(idProducto);
        if(productoConsultado.isEmpty()){
            throw new ExistenciaDeProductoException("El id del producto "+idProducto+" no existe o no es válido");
        }else {
            Producto producto = new Producto();
            producto.setId(idProducto);
            return ResponseEntity.ok(inventarioServ.obtenerInventarioDeProducto(producto));
        }
    }

    //EndPoint que actualiza el inventario de un producto
    @Operation(summary = "Actualizar inventario", description = "Actualiza la información de un inventario.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "EndPoint que actualiza el inventario de un producto.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Inventario.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna estado http 200, actualizando el inventario y retornando la información del mismo.", content = @Content(schema = @Schema(implementation = Inventario.class))),
    })
    @PutMapping
    public Inventario actualizarInventario(@RequestBody Inventario invenEdit){
        return inventarioServ.actualizarInventario(invenEdit);
    }
}
