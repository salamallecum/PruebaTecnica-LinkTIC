**Codigo Fuente que da solución a la prueba ténica LinkTIC**

**- Requisitos técnicos**  
Lenguaje de programación utilizado: Java - SpringBoot  
Motor de base datos utilizado: MySQL  
Aplicación de consumo de endPoints: PostMan  

**- Carpeta bd:** En esta carpeta encontrará el script de base de datos .sql para la creación de la estructura de tablas que usa la aplicación
			      (ejecute este script en la base de datos en caso de que el proyecto no cree la estructura de tablas automáticamente) 

**- Carpeta PostMan:** En esta carpeta encontrará un archivo .JSON con el que podrá importar a su postman la colección de peticiones necesarias para el consuma de los endpoints de la aplicación.


**A- Instrucciones de instalación**  
1- Descargue o clone con ayuda de git el presente repositorio.  
2- Importe el proyecto en el editor IntelliJ.  
3- Modifique los datos de conexión a la base de datos en el archivo application.properties ubicado en la ruta: src/main/resources  
4- En el motor de base de datos, cree una base de datos que se llame bd_pruebatecnica (allí se creará la estructura de tablas más adelante).  
5- Ejecute la clase principal del proyecto PruebaTecnicaLinkTicApplication.java ubicada en la ruta src/main/java/com.pruebaTecnica.PruebaTecnicaLinkTic, el proyecto creará 
   automáticamente la estructura de tablas en la base de datos, de esta manera ya podrá consumir los endpoints de la aplicación y hacer consulta de la documentación del proyecto
   en los siguientes links:  

    - Link de acceso a documentación Swagger: http://localhost:8080/pruebatecnica/api/v1/swagger-ui/index.html  
    - Link de acceso a documentación: http://localhost:8080/pruebatecnica/api/v1/v3/api-docs  

**B- Descripción de la arquitectura**  
El presente proyecto se desarrolló bajo la arquitectura basada en capas (también conocida como Layered Architecture) lo cual permite que el sistema se organice en capas independientes 
facilitando la implementación de la lógica de negocio, mapeo de entidades, pruebas unitarias, pruebas de integración, manejo de excepciones, mantenimiento y escalabilidad a largo plazo. 

**C- Diagrama de arquitectura
**<img width="1428" height="441" alt="Arquitectura microservicio - Gestión de productos drawio" src="https://github.com/user-attachments/assets/f56bd106-3a4a-4227-aca2-1e00163bfeb6" />

**D- Decisiones técnicas**  
- Se desarrolló el presente proyecto teniendo en cuenta las siguientes premisas:
   
	- Se implementó el endPoint comprarProducto en la clase ProductoController, de esta manera hay uniformidad en el funcionamiento de la entidad Producto en la capa
      controlador y se garantiza la integridad de funcionalidades ligadas a una misma entidad, en la función comprarProducto el objeto de tipo producto es el principal implicado porque la mayoría de sus atributos (nombre y precio) forman parte del resultado de la 
      transacción, resultaba más eficiente utilizar la dependencia que tiene la entidad Inventario con la entidad Producto a través de su atributo producto_id, para así evaluar la cantidad disponible 
      de un producto para a partir de ahí avalar o restringir mediante una excepción la compra del producto, que tener el endPoint comprarProducto en la clase InventarioController generando desorden en el manejo de la entidad producto y
      posibles mal entendidos en la escalabilidad y mantenimientos futuros de la aplicación.

    - Se implementó solo un microservicio con toda la lógica de negocio propuesta, debido a que en espacios productivos suele haber solo un microservicio de gestión de compra de productos el
      cual podría interactuar con otros microservicios encargados de manejar otras características de una aplicación, tales como, la gestión y autenticación de usuarios, Gestión de eventos, etc.  

**E- Flujo de compra implementado**  
-La aplicación se rige bajo el siguiente flujo de compra:  

  	1- El usuario ingresa el id del producto, seguido de la cantidad del producto a comprar y el dinero con el que va a pagar la compra.  
  2- El sistema valida que el id del producto corresponda con el id de un producto existente, si existe continua con el siguiente paso, de lo contrario, arrojará una excepción.  
  3- El sistema valida que la cantidad de producto solicitada por el usuario sea menor o igual a la cantidad de producto registrada en inventario, si es así, continua con el siguiente paso, de lo contrario, arrojará una excepción.  
  4- El sistema valida que el monto de dinero suministrado por el usuario sea menor o igual al valor a pagar por los productos, si es así calcula el dinero de cambio a dar al usuario, de lo contrario, arrojará una excepción.  
  5- Finalmente el sistema avala la transacción, actualiza el inventario del producto en cuestión con la diferencia entre la cantidad de producto solicitada y la cantidad de producto registrada en inventario,
     retorna el resultado de la transacción como transacción exitosa junto con la información de la compra realizada (nombre de producto, cantidad, valor unitario, valor a pagar, dinero recibido, dinero de cambio).  

**F- Uso de IA**    
Para el presente proyecto se utilizó como herramienta de inteligencia artificial Copilot y Gemini IA, su asesoría estuvo orientada en la refactorización del código de las pruebas unitarias 
y de integración, identificando posibles redundancias en el código y mejoras en la utilización de dependencias.
