package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase principal de la aplicación Spring Boot.
 */
@SpringBootApplication
@RestController
public class DemoApplication {

    /**
     * Método principal.
     * @param args argumentos de la línea de comandos
     */
    public static void main(final String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * Endpoint de health check.
     * @return String indicando el estado de la aplicación
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
