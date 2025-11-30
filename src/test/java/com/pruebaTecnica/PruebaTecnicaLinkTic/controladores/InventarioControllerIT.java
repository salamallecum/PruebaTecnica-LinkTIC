package com.pruebaTecnica.PruebaTecnicaLinkTic.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Inventario;
import com.pruebaTecnica.PruebaTecnicaLinkTic.entidades.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//Clase de prueba con las pruebas de integración realizadas a los endPoints de la clase InventarioController
public class InventarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMap;

    //Metodo de prueba del EndPoint actualizarProducto
    @DisplayName("Test Integración - actualizar cantidad de un inventario")
    @Test
    public void testActualizarInventario() throws Exception {
        //Creamos un objeto de tipo inventario con las modificaciones a aplicar al inventario existente
        Inventario inventarioActualizado = new Inventario();
        inventarioActualizado.setId(1L);
        inventarioActualizado.setCantidad(800);
        //Creamos un objeto producto con la informacion del producto a actualizar su inventario
        Producto productInvolucrado = new Producto();
        productInvolucrado.setId(1L);
        productInvolucrado.setNombre("Papas Fritas");
        productInvolucrado.setDescripcion("Papas Margarita frito lay 30gr");
        productInvolucrado.setPrecio(1200);
        //Asociamos el inventario editado al producto a consultar
        inventarioActualizado.setProducto(productInvolucrado);

        //Simulamos una petición http PUT con la información del inventario
        mockMvc.perform(put("/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMap.writeValueAsString(inventarioActualizado)))
                .andExpect(status().isOk())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Inventario actualizado OK: " + result.getResponse().getContentAsString()));
    }

    //Metodo de prueba del EndPoint obtenerCantidadDeUnProducto
    @DisplayName("Test Integración - obtener cantidad de un producto")
    @Test
    public void testObtenerCantidadDeUnProducto() throws Exception {
        //Simulamos una petición http GET con el id del inventario a consultar
        mockMvc.perform(get("/inventarios/producto/1"))
                .andExpect(status().isOk())
                //Imprimimos el resultado de la petición
                .andDo(result -> System.out.println("Inventario consultado: " + result.getResponse().getContentAsString()));
    }

}
