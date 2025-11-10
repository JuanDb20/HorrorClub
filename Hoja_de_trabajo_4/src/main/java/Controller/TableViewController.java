package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import Model.HorrorClub;
import Model.HorrorMember;
import Model.Movie;
import Model.SubGenre;
import util.AlertUtils;

import java.io.IOException;
import javafx.collections.FXCollections;

public class TableViewController {

    @FXML
    private TableView<HorrorMember> membersTable;

    @FXML
    private TableColumn<HorrorMember, String> idColumn;

    @FXML
    private TableColumn<HorrorMember, String> nameColumn;

    @FXML
    private TableColumn<HorrorMember, Integer> ageColumn;

    @FXML
    private TableColumn<HorrorMember, String> emailColumn;

    @FXML
    private TableColumn<HorrorMember, Integer> fanaticismColumn;

    @FXML
    private TableColumn<HorrorMember, String> movieColumn;

    @FXML
    private TableColumn<HorrorMember, String> subgenreColumn;

    @FXML
    private TableColumn<HorrorMember, ImageView> posterColumn;

    @FXML
    private ComboBox<Integer> fanaticismFilter;

    @FXML
    private ComboBox<SubGenre> subgenreFilter;

    @FXML
    private Label clubInfo;

    private HorrorClub club;

    @FXML
    private void initialize() {
        // Obtiene la instancia Singleton del club
        club = HorrorClub.getInstance();

        // Configura la información superior del club
        clubInfo.setText("Horror Club de Randy Meeks — Miembros registrados: " + club.getSize());

        // Configura los valores de los ComboBox
        fanaticismFilter.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        subgenreFilter.setItems(FXCollections.observableArrayList(SubGenre.values()));

        // Mapea las columnas con las propiedades de HorrorMember
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        fanaticismColumn.setCellValueFactory(new PropertyValueFactory<>("nivelFanatismo"));

        // Columnas derivadas de Movie
        movieColumn.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue().getFavoriteMovie();
            return new javafx.beans.property.SimpleStringProperty(
                    movie != null ? movie.getName() : "N/A"
            );
        });

        subgenreColumn.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue().getFavoriteMovie();
            return new javafx.beans.property.SimpleStringProperty(
                    (movie != null && movie.getSubGenre() != null) ? movie.getSubGenre().toString() : "N/A"
            );
        });

        // Imagen de la carátula (miniatura)
        posterColumn.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(35);
            }

            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Movie movie = getTableView().getItems().get(getIndex()).getFavoriteMovie();
                    if (movie != null && movie.getImageURL() != null && !movie.getImageURL().isEmpty()) {
                        try {
                            imageView.setImage(new Image("file:" + movie.getImageURL()));
                            setGraphic(imageView);
                        } catch (Exception e) {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        // Cargar los miembros registrados
        membersTable.getItems().setAll(club.getMembers());
    }

    @FXML
    private void applyFilters(ActionEvent event) {
        Integer selectedLevel = fanaticismFilter.getValue();
        SubGenre selectedSubgenre = subgenreFilter.getValue();

        var filteredList = FXCollections.observableArrayList(club.getMembers());

        if (selectedLevel != null) {
            filteredList.setAll(club.filterByLevel(selectedLevel));
        }

        if (selectedSubgenre != null) {
            filteredList.retainAll(club.filterBySubgenre(selectedSubgenre));
        }

        membersTable.setItems(filteredList);
    }

    @FXML
    private void clearFilters(ActionEvent event) {
        fanaticismFilter.setValue(null);
        subgenreFilter.setValue(null);
        membersTable.getItems().setAll(club.getMembers());
    }

    @FXML
    private void viewMemberDetail(ActionEvent event) {
        HorrorMember selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.createAlert(Alert.AlertType.WARNING, "Selección requerida",
                    "No hay ningún miembro seleccionado", "Selecciona un miembro para ver los detalles.");
            return;
        }

        // Aquí iría la lógica para abrir una nueva ventana con los detalles del miembro
        AlertUtils.createAlert(Alert.AlertType.INFORMATION, "Detalles del miembro",
                "Nombre: " + selected.getName(),
                "Película favorita: " + (selected.getFavoriteMovie() != null ? selected.getFavoriteMovie().getName() : "N/A"));
    }

    @FXML
    private void returnToMainView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void logOut(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cerrar sesión");
        confirmAlert.setHeaderText("¿Estás seguro de que deseas salir?");
        confirmAlert.setContentText("La aplicación se cerrará por completo.");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image("file:src/main/resources/images/randy-meeks.jpg"));

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                System.exit(0);
            }
        });
    }
}
