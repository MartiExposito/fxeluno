package com.java.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
/**
 * Clase principal que integra Spring Boot con JavaFX.
 * Inicia la aplicación JavaFX y configura el contexto de Spring Boot.
 */
@SpringBootApplication
public class Main extends Application {

	private static ConfigurableApplicationContext context;
	private static String[] savedArgs;
	/**
	 * Método principal para iniciar la aplicación.
	 * @param args argumentos de la línea de comandos.
	 */
	public static void main(String[] args) {
		savedArgs = args;
		Application.launch(Main.class, args);
	}

	@Override

	public void init() {

		// Configuración inicial del contexto de Spring Boot
		context = SpringApplication.run(Main.class, savedArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Configuración de la escena JavaFX
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/java/fx/main.fxml"));
		fxmlLoader.setControllerFactory(context::getBean);

		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Gestor");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		// Cerrar el contexto de Spring Boot al salir
		context.close();
	}

	/**
	 * Obtiene el contexto de la aplicación Spring.
	 * @return el contexto configurable de la aplicación.
	 */
	public static ConfigurableApplicationContext getContext() {
		return context;
	}
}
