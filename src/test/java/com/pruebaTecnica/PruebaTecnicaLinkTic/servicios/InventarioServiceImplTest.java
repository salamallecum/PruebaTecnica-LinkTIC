package com.pruebaTecnica.PruebaTecnicaLinkTic.servicios;

import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.InventarioRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.repositorios.ProductoRepository;
import com.pruebaTecnica.PruebaTecnicaLinkTic.servicios.impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//Clase de prueba que permite probar la implementación de la clase InventarioService con sus respectivos escenarios mediante pruebas unitarias
public class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepo;

    @Mock
    private ProductoRepository productoRepo;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;
    private Inventario inventario;

    //Creamos el producto que se utilizará para las pruebas con su respectivo inventario
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop Acer premium");
        producto.setPrecio(1000);
        inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(10);
    }

    //Metodo de prueba que consulta el inventario de un producto
    @DisplayName("Consulta de inventario de un producto")
    @Test
    public void testObtenerInventarioDeProducto() {
        //Simulamos que el repositorio devuelve el inventario
        when(inventarioRepo.findByProducto(producto)).thenReturn(inventario);
        Inventario resultado = inventarioService.obtenerInventarioDeProducto(producto);

        //Validamos que el inventario no venga nulo y que la cantidad esperada sea 10
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(10);
        //Imprimimos el resultado de la prueba
        System.out.println("Inventario esperado para producto '" + producto.getNombre() + " : " + resultado.getCantidad());
    }

    //Metodo de prueba que permite actualizar el inventario de un producto
    @DisplayName("Actualización de inventario")
    @Test
    public void testActualizarInventario() {
        //Modificacion de cantidad en inventario
        inventario.setCantidad(10);

        //Simulamos que el repositorio guarda y devuelve el inventario actualizado
        when(inventarioRepo.save(inventario)).thenReturn(inventario);
        Inventario resultado = inventarioService.actualizarInventario(inventario);

        //Validamos que el inventario no retorne nulo y que la cantidad sea 5, la esperada
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(10);
        //Imprimimos el resultado de la prueba
        System.out.println("Inventario actualizado para producto '" + producto.getNombre() + "': " + resultado.getCantidad());
        // Verificamos que se llamó al repositorio
        verify(inventarioRepo, times(1)).save(inventario);
    }
}
