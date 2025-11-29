package com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Interfaz que define los metodos jpa para las transacciones de los inventarios
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    //Metodo para consultar un inventario teniendo en cuenta el id del producto
    Inventario findByProducto(Producto producto);

}
