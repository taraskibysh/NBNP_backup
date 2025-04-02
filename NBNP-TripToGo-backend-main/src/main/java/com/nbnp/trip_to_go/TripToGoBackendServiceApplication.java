package com.nbnp.trip_to_go;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Trip To Go API", version = "1.0", description = "API Documentation"))
public class TripToGoBackendServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripToGoBackendServiceApplication.class, args);
	}

}
