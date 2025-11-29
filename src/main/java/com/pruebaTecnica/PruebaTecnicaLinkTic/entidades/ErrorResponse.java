package com.pruebaTecnica.PruebaTecnicaLinkTic.entidades;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//Clase que modela las respuestas de las excepciones del sistema para su documentación
@Getter
@Setter
@Schema(name = "ErrorResponse", description = "Estructura estándar para errores")
public class ErrorResponse {

    @Schema(description = "Mensaje descriptivo del error", example = "El producto con id 123 no existe")
    private String message;

    @Schema(description = "Timestamp del error", example = "2025-11-29T10:41:36.27495")
    private String timestamp;
}
