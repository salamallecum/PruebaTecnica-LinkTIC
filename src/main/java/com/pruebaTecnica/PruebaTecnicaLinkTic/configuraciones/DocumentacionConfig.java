package com.pruebaTecnica.PruebaTecnicaLinkTic.configuraciones;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

//Clase encargada de la configuracion e información principal de la aplicación que tendrá la documentación Swagger
@OpenAPIDefinition(
        info = @Info(
                //Info de la aplicacion e info de contacto del desarrollador
                title = "Prueba técnica LinkTIC",
                description = "Api REST con la lógica de negocio propuesta por la prueba técnica LinkTIC.",
                version = "1.0.0",
                contact = @Contact(name = "Alejo Amaya", url = "https://miportafolioweb.free.nf", email = "luisalejandroamayatorres@gmail.com")
        ),
        servers = {
                @Server(description = "DEV SERVER", url = "http://localhost:8080")
        },
        security = @SecurityRequirement(name = "Security token") //Aqui definimos el esquema de seguridad que tendra la documentacion
)
/*//Define el esquema de seguridad de la documentacion
@SecurityScheme(
        name = "Security token",
        description = "Token de acceso de la API",
        type = SecuritySchemeType.HTTP, //Indica que estamos trabajando con tokens
        in = SecuritySchemeIn.HEADER,
        scheme = "Bearer",
        bearerFormat = "JWT"
)*/
public class DocumentacionConfig {}

