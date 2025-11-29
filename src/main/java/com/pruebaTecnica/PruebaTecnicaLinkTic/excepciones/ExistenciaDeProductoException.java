package com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones;

//Clase encargada del manejo de la excepcion de existencia previa de productos
public class ExistenciaDeProductoException extends RuntimeException{

    public ExistenciaDeProductoException(String mensaje){
        super(mensaje);
    }
}
