package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//Clase de prueba que permite probar los EndPoints de la clase InventarioController
public class InventarioControllerTest {

    @Mock
    private InventarioServiceImpl inventarioServ;

    @Mock
    private ProductoServiceImpl productoServ;

    @InjectMocks
    private InventarioController inventarioControl;

    private Producto producto;
    private Inventario inventario;

    //Creamos los productos que se utilizarán para las pruebas con su respectivo inventario
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(5000);
        inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(50);
    }

    //Metodo de prueba del EndPoint obtenerCantidadDeUnProducto cuando un producto esta existe
    @DisplayName("EndPoint obtener cantidad de un producto cuando existe")
    @Test
    public void testObtenerCantidadDeUnProducto_CuandoExiste() {

        //Simulamos que el producto existe y tiene inventario
        when(productoServ.getProductoById(1L)).thenReturn(Optional.of(producto));
        when(inventarioServ.obtenerInventarioDeProducto(any(Producto.class))).thenReturn(inventario);
        ResponseEntity<?> response = inventarioControl.obtenerCantidadDeUnProducto(1L);

        //Validamos que el metodo de la clase controlador retorne estado http 200 y la cantidad de producto esperada
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(((Inventario) response.getBody()).getCantidad()).isEqualTo(50);

        //Imprimimos el resultado de la prueba
        System.out.println("Inventario obtenido para el producto '" + producto.getNombre() +
                "': " + ((Inventario) response.getBody()).getCantidad());
    }

    //Metodo de prueba del EndPoint obtenerCantidadDeUnProducto cuando un producto NO existe o es igual a 0
    @DisplayName("EndPoint obtener cantidad de un producto cuando NO existe")
    @Test
    public void testObtenerCantidadDeUnProducto_CuandoNoExiste() {
        //Simulamos que el producto no exista
        when(productoServ.getProductoById(99L)).thenReturn(Optional.empty());
        //Validamos que al llamar el metodo del controlador retorne la excepcion ExistenciaDeProductoException
        assertThrows(ExistenciaDeProductoException.class,
                () -> inventarioControl.obtenerCantidadDeUnProducto(99L));
        //Imprimimos el resultado de la prueba
        System.out.println("Se lanzó la excepción ExistenciaDeProductoException como se esperaba");
    }


    //Metodo de prueba del EndPoint actualizarInventario
    @DisplayName("EndPoint actualizar inventario")
    @Test
    public void testActualizarInventario() {
        //Modificamos el inventario del producto
        inventario.setCantidad(30);

        //Validamos que al ejecutarse el metodo de la clase controller de forma correcta, retorne el objeto inventario
        when(inventarioServ.actualizarInventario(inventario)).thenReturn(inventario);
        Inventario resultado = inventarioControl.actualizarInventario(inventario);
        //Validamos que el resultado no sea nulo y que la cantidad del inventario sea la esperada
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(30);
        //Imprimimos el resultado de la prueba
        System.out.println("Inventario actualizado para producto '" + producto.getNombre() +
                "': " + resultado.getCantidad());
        //Validamos que se llame solo una vez el metodo
        verify(inventarioServ, times(1)).actualizarInventario(inventario);
    }
}