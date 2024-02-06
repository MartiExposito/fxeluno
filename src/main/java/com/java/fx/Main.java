package com.java.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {

	private static ConfigurableApplicationContext context;
	private static String[] savedArgs;

	public static void main(String[] args) {
		savedArgs = args;
		Application.launch(Main.class, args);
	}

	@Override
	public void init() {
		context = SpringApplication.run(Main.class, savedArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/java/fx/main.fxml"));
		fxmlLoader.setControllerFactory(context::getBean);

		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Gestor");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		context.close();
	}


	public static ConfigurableApplicationContext getContext() {
		return context;
	}
}
