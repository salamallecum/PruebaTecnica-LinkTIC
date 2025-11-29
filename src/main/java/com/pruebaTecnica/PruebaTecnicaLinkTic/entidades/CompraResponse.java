package com.pruebaTecnica.PruebaTecnicaLinkTic.entidades;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//Clase que modela la respuesta al realizar la compra de un producto en el sistema para su documentación
@Getter
@Setter
@Schema(name = "CompraResponse", description = "Estructura del resultado de compra")
public class CompraResponse {

    @Schema(description = "Estado de la transacción", example = "Transacción exitosa")
    private String estadoDeTransaccion;

    @Schema(description = "Nombre del producto", example = "Arroz")
    private String nombreProducto;

    @Schema(description = "Cantidad solicitada de producto", example = "2")
    private Integer cantidad;

    @Schema(description = "Valor del producto por unidad", example = "20000")
    private Integer valorUnitario;

    @Schema(description = "Valor a pagar por el producto", example = "40000")
    private Integer valorAPagar;

    @Schema(description = "Dinero recibido como pago por el producto", example = "41000")
    private Integer dineroRecibido;

    @Schema(description = "Dinero sobrante por la compra del producto", example = "1000")
    private Integer cambio;
}
