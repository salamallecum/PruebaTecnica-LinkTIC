package com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones;

//Clase encargada del manejo de la excepcion de insuficiencia de inventario
public class InventarioInsuficienteException extends RuntimeException{

    public InventarioInsuficienteException(String mensaje){
        super(mensaje);
    }


}
