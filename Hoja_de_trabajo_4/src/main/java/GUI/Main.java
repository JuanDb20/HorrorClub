package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml")); //Carga la pantalla de MainView.fxml

            Scene scene = new Scene(root);

            //Me permite configurar la ventana
            primaryStage.setTitle("El Club del Horror de Randy Meeks");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ups..Error al cargar la interfaz: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args); //Con esto deberia iniciar la app de JavaFX
    }

    public static void openWindow(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("HorrorClub - Nueva Ventana");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/randy-meeks.jpg")));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}