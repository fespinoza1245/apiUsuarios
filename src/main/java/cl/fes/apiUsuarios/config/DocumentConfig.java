package cl.fes.apiUsuarios.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Documentacion CRUD API Usuarios")
                        .description("Este CRUD permite realizar las operaciones basicas de un CRUD y estan controladas las excepciones")
                        );


    }
}
