package edu.unimagdalena.lms2;

import org.springframework.boot.SpringApplication;

public class TestLms2Application {

	public static void main(String[] args) {
		SpringApplication.from(Lms2Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
