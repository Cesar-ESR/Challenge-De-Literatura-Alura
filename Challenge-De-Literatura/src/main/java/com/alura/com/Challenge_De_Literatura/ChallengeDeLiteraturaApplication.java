package com.alura.com.Challenge_De_Literatura;

import com.alura.com.Challenge_De_Literatura.Main.Main;
import com.alura.com.Challenge_De_Literatura.Reposiroty.AutorRepository;
import com.alura.com.Challenge_De_Literatura.Reposiroty.LibroRepository;
import com.alura.com.Challenge_De_Literatura.Service.AutorService;
import com.alura.com.Challenge_De_Literatura.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ChallengeDeLiteraturaApplication implements CommandLineRunner {

	private final Main main;

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private AutorService autorService;

	@Autowired
	private LibroService libroService;

	public ChallengeDeLiteraturaApplication (Main main){

		this.main = main;

	}

	public static void main(String[] args) {
		SpringApplication.run(ChallengeDeLiteraturaApplication.class, args);

	}

	@Override
	public void run(String[] args) throws Exception{
		Main main = new Main(libroService, autorService, libroRepository, autorRepository);
		main.showMenu();


	}

}
