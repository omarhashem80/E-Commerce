package com.ecommerce.inventory;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Inventory microservice REST API Documentation",
                description = "E-Commerce Inventory microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Omar Hashem",
                        email = "omarsayed4736@gmail.com",
                        url = "https://www.linkedin.com/in/omar-hashem-32128a252"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.linkedin.com/in/omar-hashem-32128a252"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "E-Commerce Inventory microservice REST API Documentation",
                url = "https://www.linkedin.com/in/omar-hashem-32128a252"
        )
)
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

}
