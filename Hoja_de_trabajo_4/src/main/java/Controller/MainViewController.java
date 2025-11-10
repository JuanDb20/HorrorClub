package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    // Botón para ir al formulario de registro
    @FXML
    void registerMember(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/RegisterView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ups... Hay un error al abrir la pantalla de registro: " + e.getMessage());
        }
    }

    // Botón para ir a la lista de miembros
    @FXML
    void viewMembers(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/TableView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ups... Hay un error al abrir la pantalla de vista de miembros: " + e.getMessage());
        }
    }
}


