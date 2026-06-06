package br.com.CineTicket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Console {
    public static void main(String[] args) {
        // Este comando ativa o Spring Boot, o Flyway e o servidor web embutido
        SpringApplication.run(Console.class, args);
    }
}