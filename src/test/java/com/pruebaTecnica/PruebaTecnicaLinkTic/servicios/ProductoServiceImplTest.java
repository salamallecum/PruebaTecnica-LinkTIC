package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.ExistenciaDeProductoException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.FondosInsuficientesException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones.InventarioInsuficienteException;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.InventarioRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.ProductoRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//Clase de prueba que permite probar la implementación de la clase ProductoService con sus respectivos escenarios mediante pruebas unitarias
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private InventarioRepository inventarioRepo;

    @InjectMocks
    private ProductoServiceImpl productoService;

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
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop Acer Premium");
        producto.setPrecio(1000);
        inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(10);

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Reproductor Mp3");
        producto1.setDescripcion("Ipod Nano");
        producto1.setPrecio(500);
        inventario1 = new Inventario();
        inventario1.setProducto(producto1);
        inventario1.setCantidad(20);
    }

    //Metodo de prueba que permite probar el registro de productos cuando ya existe un producto con el nombre indicado
    @DisplayName("Registro de producto cuando ya existe previamente")
    @Test
    public void testSaveProducto_CuandoYaExiste() {
        //Simulamos la existencia del producto con ese nombre
        when(productoRepo.findByNombre("Laptop")).thenReturn(Optional.of(producto));
        assertThrows(ExistenciaDeProductoException.class,
                () -> productoService.saveProducto(producto, inventario));
        //Verificamos que no se guardó nada (ni el inventario ni el producto)
        verify(inventarioRepo, never()).save(any(Inventario.class));
        verify(productoRepo, never()).save(any(Producto.class));
        //Imprimimos el resultado de la prueba
        System.out.println("Se lanzó la excepción ExistenciaDeProductoException como se esperaba");

    }

    //Metodo de prueba que permite probar el registro de productos cuando NO existe un producto con el nombre indicado
    @DisplayName("Registro de producto cuando no existe previamente")
    @Test
    public void testSaveProducto_CuandoNoExiste() {
        //Simulamos que no existe un producto con ese nombre
        when(productoRepo.findByNombre("NuevoProducto")).thenReturn(Optional.empty());
        //Guardamos el producto y su inventario
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);
        when(inventarioRepo.save(any(Inventario.class))).thenReturn(inventario);
        ResponseEntity<?> response = productoService.saveProducto(producto, inventario);
        //Validamos que al registrar el producto la solicitud Http retorne estado 201
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(producto);
        //Verificamos que se guardaron ambos
        verify(inventarioRepo, times(1)).save(inventario);
        verify(productoRepo, times(1)).save(producto);
        //Imprimimos el resultado de la prueba
        System.out.println("Producto guardado correctamente: " + response.getBody());
        System.out.println("Código de respuesta: " + response.getStatusCodeValue());
    }

    //Metodo de prueba que permite probar la consulta de los productos que se registran
    @DisplayName("Consulta de productos registrados")
    @Test
    public void testGetAllProductos() {
        //Indicamos que cuando ejecute el metodo de consulta, retorne los objetos creados mediante un array
        when(productoRepo.findAll()).thenReturn(Arrays.asList(producto, producto1));
        List<Producto> productos = productoService.getAllProductos();
        //Verificamos el tamaño del array corresponda con el número de productos creados y sus nombres de producto sean P1 y P2
        assertThat(productos).hasSize(2);
        assertThat(productos).extracting("nombre").contains("Laptop", "Reproductor Mp3");
        //Imprimimos el resultado de la prueba
        System.out.println("Productos obtenidos: " + productos.size());
        productos.forEach(p -> System.out.println(" - " + p.getNombre()));
    }

    //Metodo de prueba para la consulta de un producto mediante su id
    @DisplayName("Consulta de producto mediante su id")
    @Test
    public void testGetProductoById() {
        //Obtenemos el objeto con ayuda del metodo definido en la implementacion
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        Optional<Producto> resultado = productoService.getProductoById(1L);
        //Validamos que el objeto exista y su nombre corresponde con el nombre esperado Prueba
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Laptop");
        //Imprimimos el resultado de la prueba
        System.out.println("Producto encontrado por ID: " + resultado.get().getNombre());
    }

    //Metodo de prueba para la compra de un producto inexistente
    @DisplayName("Compra de producto inexistente")
    @Test
    public void testComprarProductoNoExiste() {
        when(productoRepo.findById(3L)).thenReturn(Optional.empty());
        //Validamos que el id de producto no exista para retornar la excepcion
        Exception ex = assertThrows(ExistenciaDeProductoException.class,
                () -> productoService.comprarProducto(3L, 2, 3000));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (producto no existe): " + ex.getMessage());
    }

    //Metodo de prueba para la compra de un producto con inventario insuficiente
    @DisplayName("Compra de producto con inventario insuficiente")
    @Test
    public void testComprarProductoInventarioInsuficiente() {
        //Validamos la existencia del producto e inventario
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepo.findByProducto(any())).thenReturn(inventario);
        //Simulamos la compra excesiva del producto para retornar la excepcion
        Exception ex = assertThrows(InventarioInsuficienteException.class,
                () -> productoService.comprarProducto(1L, 20, 5000));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (inventario insuficiente): " + ex.getMessage());
    }

    //Metodo de prueba para la compra de un producto con dinero insuficiente
    @DisplayName("Compra de producto con dinero insuficiente")
    @Test
    public void testComprarProductoFondosInsuficientes() {
        //Validamos la existencia del producto e inventario
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepo.findByProducto(any())).thenReturn(inventario);
        //Simulamos la compra del producto con poco dinero para retornar la excepcion
        Exception ex = assertThrows(FondosInsuficientesException.class,
                () -> productoService.comprarProducto(1L, 2, 500));
        //Imprimimos el resultado de la prueba
        System.out.println("Resultado esperado (fondos insuficientes): " + ex.getMessage());
    }

    //Metodo de prueba para la compra de un producto con dinero insuficiente
    @DisplayName("Compra de producto exitosa")
    @Test
    public void testComprarProductoExitoso() {
        //Validamos la existencia del producto e inventario
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepo.findByProducto(any())).thenReturn(inventario);
        //Simulamos la compra
        ResponseEntity<?> response = productoService.comprarProducto(1L, 2, 2000);
        //Imprimimos los resultados de la prueba y verificamos que la respuesta no sea nula
        assertNotNull(response);
        System.out.println("Resultado esperado (compra exitosa): " + response.getBody());
    }
}
