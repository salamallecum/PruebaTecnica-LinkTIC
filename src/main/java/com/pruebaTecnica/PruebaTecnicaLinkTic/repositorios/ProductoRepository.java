package com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Interfaz que define los metodos jpa para las transacciones de los productos
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //Metodo para consultar un producto por su nombre
    Optional<Producto> findByNombre(String email);
}
