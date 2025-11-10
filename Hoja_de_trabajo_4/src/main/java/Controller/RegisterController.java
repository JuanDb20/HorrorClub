package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisterController {

    @FXML private TextField idField;
    @FXML private TextField nameButton;
    @FXML private TextField lastNameButton;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField ageButton;
    @FXML private TextField emailButton;
    @FXML private Spinner<Integer> fanLevelSpinner;

    @FXML private TextField movieNameField;
    @FXML private Spinner<Integer> movieYearSpinner;
    @FXML private ComboBox<SubGenre> subgenreCombo;
    @FXML private TextField movieImageUrlField;

    @FXML private CheckBox acceptedCheck;
    @FXML private Spinner<Double> scoreSpinner;
    @FXML private TextArea commentArea;

    @FXML private Button registerButton;
    @FXML private Button backButton;


    private final HorrorClub club = HorrorClub.getInstance();


    @FXML
    private void initialize() {
        idField.setText(UUID.randomUUID().toString());

        fanLevelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
        fanLevelSpinner.setEditable(true);

        int currentYear = LocalDate.now().getYear();
        movieYearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, currentYear, currentYear));
        movieYearSpinner.setEditable(true);

        scoreSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 7.5, 0.5));
        scoreSpinner.setEditable(true);
        scoreSpinner.getEditor().setTextFormatter(new TextFormatter<>(c -> {
            try { Double.parseDouble(c.getControlNewText()); }
            catch (Exception e) { if (c.getControlNewText().isEmpty()) return c; return null; }
            return c;
        }));

        subgenreCombo.getItems().setAll(SubGenre.values());
        subgenreCombo.setConverter(new StringConverter<>() {
            @Override public String toString(SubGenre sg) { return sg == null ? "" : sg.name(); }
            @Override public SubGenre fromString(String s) { return s == null ? null : SubGenre.valueOf(s); }
        });

        birthdatePicker.valueProperty().addListener((obs, oldV, newV) -> {
            Integer age = calculateAgeFromBirthDate(newV);
            ageButton.setText(age == null ? "" : String.valueOf(age));
        });

        registerButton.setOnAction(e -> onRegister());
        backButton.setOnAction(e -> goBack());

    }

    private void onRegister() {
        List<String> errores = new ArrayList<>();

        String name = trimOrNull(nameButton.getText());
        if (name == null) errores.add("name es requerido");

        String lastName = trimOrNull(lastNameButton.getText());
        if (lastName == null) errores.add("lastName es requerido");

        LocalDate bd = birthdatePicker.getValue();
        if (bd == null) {
            errores.add("birthdate es requerido");
        } else {
            if (!bd.isBefore(LocalDate.now())) {
                errores.add("birthdate debe ser anterior a hoy");
            }
            Integer calc = calculateAgeFromBirthDate(bd);
            if (calc == null || calc <= 0) errores.add("age calculado desde birthdate debe ser mayor que 0");
        }

        String email = trimOrNull(emailButton.getText());
        if (email == null) {
            errores.add("email es requerido");
        } else if (!isValidEmail(email)) {
            errores.add("email no tiene formato valido");
        }

        Integer fan = fanLevelSpinner.getValue();
        if (fan == null || fan < 1 || fan > 5) errores.add("levelFanaticism debe estar entre 1 y 5");

        String mvName = trimOrNull(movieNameField.getText());
        if (mvName == null) errores.add("movie.name es requerido");

        Integer mvYear = movieYearSpinner.getValue();
        int currentYear = LocalDate.now().getYear();
        if (mvYear == null) {
            errores.add("movie.yearLaunch es requerido");
        } else if (mvYear <= 1800 || mvYear > currentYear) {
            errores.add("movie.yearLaunch debe estar entre 1801 y " + currentYear);
        }

        SubGenre sg = subgenreCombo.getValue();
        if (sg == null) errores.add("movie.subGenre es requerido");

        String imgUrl = trimOrNull(movieImageUrlField.getText());
        if (imgUrl == null) {
            errores.add("movie.imageURL es requerido");
        } else if (!isValidURL(imgUrl)) {
            errores.add("movie.imageURL no es una url valida");
        }

        Double score = scoreSpinner.getValue();
        if (score == null || score < 0 || score > 10) errores.add("evaluation.score debe estar entre 0 y 10");

        String comment = trimOrNull(commentArea.getText());
        if (comment == null) errores.add("evaluation.comment es requerido");

        if (! errores.isEmpty()) {
            showError(String.join("\n", errores));
            return;
        }

        try {
            Movie movie = new Movie();
            movie.setName(mvName);
            movie.setYearLaunch(mvYear);
            movie.setSubGenre(sg);
            movie.setImageURL(imgUrl);

            Evaluation evaluation = new Evaluation(
                    acceptedCheck.isSelected(),
                    comment,
                    score
            );

            HorrorMember member = new HorrorMember();
            member.setName(name);
            member.setLastName(lastName);
            member.setBirthDate(bd);
            member.setEmail(email);
            member.setLevelFanaticism(fan);
            member.setFavoriteMovie(movie);
            member.setEvaluation(evaluation);

            if (!member.isValidInfo()) {
                showError("el modelo rechazo la informacion. revisa birthdate, email, niveles, movie y evaluation");
                return;
            }

            club.addMember(member);
            showInfo("miembro registrado. total miembros: " + club.getSize());
            resetForm();

        } catch (IllegalArgumentException ex) {
            showError("no se pudo registrar: " + ex.getMessage());
        } catch (Exception ex) {
            showError("error inesperado: " + ex.getMessage());
        }
    }


    private void resetForm() {
        idField.setText(UUID.randomUUID().toString());
        nameButton.clear();
        lastNameButton.clear();
        birthdatePicker.setValue(null);
        ageButton.clear();
        emailButton.clear();
        fanLevelSpinner.getValueFactory().setValue(3);

        movieNameField.clear();
        movieYearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
        subgenreCombo.getSelectionModel().clearSelection();
        movieImageUrlField.clear();

        acceptedCheck.setSelected(false);
        scoreSpinner.getValueFactory().setValue(7.5);
        commentArea.clear();
    }

    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static Integer calculateAgeFromBirthDate(LocalDate bd) {
        if (bd == null) return null;
        LocalDate today = LocalDate.now();
        int age = today.getYear() - bd.getYear();
        if (today.getMonthValue() < bd.getMonthValue() ||
                (today.getMonthValue() == bd.getMonthValue() && today.getDayOfMonth() < bd.getDayOfMonth())) {
            age--;
        }
        return Math.max(age, 0);
    }

    private static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private static boolean isValidURL(String url) {
        if (url == null || url.trim().isEmpty()) return false;
        try { new URL(url); return true; }
        catch (MalformedURLException e) { return false; }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("El Club del Horror de Randy Meeks");
            stage.show();

            ((Stage) backButton.getScene().getWindow()).close();
        } catch (Exception ex) {
            showError("No se pudo volver: " + ex.getMessage());
        }
    }

}


