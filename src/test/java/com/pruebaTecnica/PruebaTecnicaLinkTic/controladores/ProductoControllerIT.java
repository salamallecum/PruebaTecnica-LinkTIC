package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//Clase de prueba con las pruebas de integración realizadas a los endPoints de la clase ProductoController
public class ProductoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMap;

    //Metodo de prueba del EndPoint listarProductos
    @DisplayName("Test Integración - listar productos")
    @Test
    public void testListarProductos() throws Exception {
        //Simulamos una petición http GET que consulta los productos registrados
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Productos registrados: " + result.getResponse().getContentAsString()));
    }

    //Metodo de prueba del EndPoint registrarProducto
    @DisplayName("Test Integración - registrar producto")
    @Test
    public void testRegistrarProducto() throws Exception {
        //Construimos un objeto producto
        Producto producto = new Producto();
        producto.setNombre("Peras");
        producto.setDescripcion("Peras de alta calidad");
        producto.setPrecio(2000);
        //Simulamos una petición http POST y enviamos el objeto producto construido en formato JSON
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMap.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Producto registrado OK: " + result.getResponse().getContentAsString()));
    }

    //Metodo de prueba del EndPoint registrarProducto
    @DisplayName("Test Integración - consultar producto mediante Id")
    @Test
    public void testObtenerProducto() throws Exception {
        //Simulamos una petición http GET que consulta el producto registrado
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Obtener producto x Id: " + result.getResponse().getContentAsString()));
    }

    //Metodo de prueba del EndPoint comprarProducto
    @DisplayName("Test Integración - comprar producto")
    @Test
    public void testComprarProducto() throws Exception {
        //Simulamos una petición http POST con la información de compra
        mockMvc.perform(post("/productos/comprar/1/2/5000"))
                .andExpect(status().isOk())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Compra exitosa: " + result.getResponse().getContentAsString()));
    }

}

