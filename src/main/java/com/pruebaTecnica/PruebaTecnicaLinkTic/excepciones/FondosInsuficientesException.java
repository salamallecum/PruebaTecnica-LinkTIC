package com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones;

//Clase encargada del manejo de la excepcion para fondos insufientes en la compra de un producto
public class FondosInsuficientesException extends RuntimeException{

    public FondosInsuficientesException(String mensaje){
        super(mensaje);
    }
}
