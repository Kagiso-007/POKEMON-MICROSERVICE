package com.kagiso.pokemon;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(PokemonApplication.class, args);
	}

}