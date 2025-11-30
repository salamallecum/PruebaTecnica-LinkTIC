package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.FondosInsuficientesException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.InventarioInsuficienteException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//Clase de prueba que permite probar los EndPoints de la clase ProductoController
public class ProductoControllerTest {

    @Mock
    private ProductoServiceImpl productoServ;

    @Mock
    private InventarioServiceImpl inventarioServ;

    @InjectMocks
    private ProductoController productoControl;

    private Producto producto;
    private Inventario inventario;
    private Producto producto1;
    private Inventario inventario1;

    //Creamos los productos que se utilizarán para las pruebas con su respectivo inventario
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Xbox 360");
        producto.setPrecio(15000);
        inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(10);

        producto1 = new Producto();
        producto1.setId(2L);
        producto1.setNombre("Amazon Alexa");
        producto1.setPrecio(5300);
        inventario1 = new Inventario();
        inventario1.setProducto(producto1);
        inventario1.setCantidad(20);
    }

    //Metodo de prueba del EndPoint listarProductos
    @DisplayName("EndPoint listar productos")
    @Test
    public void testListarProductos() {
        //Obtenemos los objetos simulados mediante un arreglo
        when(productoServ.getAllProductos()).thenReturn(Arrays.asList(producto, producto1));
        //Invocamos el metodo listarProductos de la clase controlador
        ResponseEntity<?> response = productoControl.listarProductos();
        //Validamos que la petición realizada al metodo retorne el estado http 200
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        //Obtenemos el listado de productos y validamos que tenga la cantidad esperada
        List<Producto> productos = (List<Producto>) response.getBody();
        assertThat(productos).hasSize(2);
        //Imprimimos los resultados de la prueba
        System.out.println("Tamaño del listado de productos: " + productos.size());
        productos.forEach(p -> System.out.println(" - " + p.getNombre()));
    }

    //Metodo de prueba del EndPoint obtenerProducto cuando un producto se consulta de forma exitosa
    @DisplayName("EndPoint obtener producto mediante Id exitoso")
    @Test
    public void testObtenerProductoPorId() {
        //Simulamos la consulta de un producto
        when(productoServ.getProductoById(1L)).thenReturn(Optional.of(producto));
        ResponseEntity<?> response = productoControl.obtenerProducto(1L);
        //Validamos que la petición realizada al metodo retorne el estado http 200
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Optional<Producto> resultado = (Optional<Producto>) response.getBody();
        //Imprimimos los resultados de la prueba
        assertThat(resultado).isPresent();
        System.out.println("Producto obtenido por ID "+resultado.get().getId()+" : " + resultado.get().getNombre());
    }

    //Metodo de prueba del EndPoint obtenerProducto cuando un producto no existe
    @DisplayName("EndPoint obtener producto mediante Id inexistente")
    @Test
    public void testObtenerProductoPorIdInexistente() {
        when(productoServ.getProductoById(3L)).thenReturn(Optional.empty());
        //Simulamos la consulta de un id inexistente para retornar la excepcion
        Exception ex = assertThrows(ExistenciaDeProductoException.class,
                () -> productoControl.obtenerProducto(3L));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (producto no existe): " + ex.getMessage());
    }

    //Metodo de prueba del EndPoint comprarProducto cuando el dinero recibido es insuficiente
    @DisplayName("EndPoint comprar producto con fondos insuficientes")
    @Test
    public void testComprarProductoFondosInsuficientes() {
        when(productoServ.comprarProducto(1L, 2, 1000))
                .thenThrow(new FondosInsuficientesException("Fondos insuficientes para la compra."));
        //Simulamos la compra de un producto con dinero insuficiente para retornar la excepcion
        Exception ex = assertThrows(FondosInsuficientesException.class,
                () -> productoControl.comprarProducto(1L, 2, 1000));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (fondos insuficientes): " + ex.getMessage());
    }

    //Metodo de prueba del EndPoint comprarProducto cuando el inventario del producto solicitado es insuficiente
    @DisplayName("EndPoint comprar producto con inventario insuficiente")
    @Test
    public void testComprarProductoInventarioInsuficiente() {
        when(productoServ.comprarProducto(1L, 20, 300000))
                .thenThrow(new InventarioInsuficienteException("Inventario insuficiente."));
        //Simulamos la compra de un producto con dinero insuficiente para retornar la excepcion
        Exception ex = assertThrows(InventarioInsuficienteException.class,
                () -> productoControl.comprarProducto(1L, 20, 300000));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (inventario insuficiente): " + ex.getMessage());
    }
}
