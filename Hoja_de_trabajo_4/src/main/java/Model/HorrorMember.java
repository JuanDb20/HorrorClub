package Model;

import java.time.LocalDate;
import java.util.UUID;

public class HorrorMember {

    private String id;
    private String name;
    private String lastName;
    private LocalDate birthdate;
    private int age;
    private String email;
    private int levelFanaticism;
    private Movie favoriteMovie;
    private Evaluation evaluation;

    public HorrorMember() {
        this.id = UUID.randomUUID().toString();
    }

    public HorrorMember(String id, String name, String lastName, LocalDate birthdate, int age,
                        String email, int levelFanaticism, Movie favoriteMovie, Evaluation evaluation) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.age = age;
        this.email = email;
        this.levelFanaticism = levelFanaticism;
        this.favoriteMovie = favoriteMovie;
        this.evaluation = null;
    }

    public String getId() {
        return id;
    }

    /** Opcional: por si quieres inyectar un id desde la UI */
    public void setId(String id) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return name + " " + lastName;
    }

    public LocalDate getBirthdate() { return birthdate; }

    public void setBirthDate(LocalDate bd){
        this.birthdate = bd;
        if (bd != null) this.age = calculateAgeFromBirthDate(bd);
    }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getLevelFanaticism() { return levelFanaticism; }
    public void setLevelFanaticism(int levelFanaticism) {
        if (levelFanaticism < 1 || levelFanaticism > 5) {
            throw new IllegalArgumentException("El nivel de fanatismo debe estar entre 1 y 5.");
        }
        this.levelFanaticism = levelFanaticism;
    }

    public Movie getFavoriteMovie() { return favoriteMovie; }
    public void setFavoriteMovie(Movie favoriteMovie) { this.favoriteMovie = favoriteMovie; }

    public Evaluation getEvaluation() { return evaluation; }
    public void setEvaluation(Evaluation evaluation) { this.evaluation = evaluation; }

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public int calculateAgeFromBirthDate(LocalDate bd) {
        if (bd == null) return 0;
        LocalDate today = LocalDate.now();
        int age = today.getYear() - bd.getYear();
        if (today.getMonthValue() < bd.getMonthValue() ||
                (today.getMonthValue() == bd.getMonthValue() && today.getDayOfMonth() < bd.getDayOfMonth())) {
            age--;
        }
        return Math.max(age, 0);
    }

    public boolean isValidInfo() {
        if (name == null || name.isBlank()) return false;
        if (lastName == null || lastName.isBlank()) return false;

        if (birthdate == null || !birthdate.isBefore(LocalDate.now())) return false;

        int calcAge = calculateAgeFromBirthDate(birthdate);
        if (calcAge <= 0) return false;

        if (!isValidEmail(email)) return false;
        if (levelFanaticism < 1 || levelFanaticism > 5) return false;

        if (favoriteMovie == null || !favoriteMovie.isValidInfoMovie()) return false;
        if (evaluation == null || !evaluation.isValidInfoEvaluation()) return false;

        return true;
    }

    public boolean isAdult() { return age >= 18; }

    public void updateEvaluation(Evaluation e) { this.evaluation = e; }

    @Override
    public String toString() {
        return "HorrorMember{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate=" + birthdate +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", levelFanaticism=" + levelFanaticism +
                ", favoriteMovie=" + favoriteMovie +
                '}';
    }
}
