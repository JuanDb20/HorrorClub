package Model;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

//Representa una pelicula de terror favorita de un miembro
public class Movie {
    private String name;
    private int yearLaunch;
    private SubGenre subGenre;
    private String imageURL;

    public Movie(String name, int yearLaunch, SubGenre subGenre, String imageURL) {
        this.name = name;
        this.yearLaunch = yearLaunch;
        this.subGenre = subGenre;
        this.imageURL = imageURL;
    }

    public Movie() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearLaunch() {
        return yearLaunch;
    }

    public void setYearLaunch(int yearLaunch) {
        this.yearLaunch = yearLaunch;
    }

    public SubGenre getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(SubGenre subGenre) {
        this.subGenre = subGenre;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isValidInfoMovie(){
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        int currentYear = LocalDate.now().getYear();
        if (yearLaunch <= 1800 || yearLaunch > currentYear) {
            return false;
        }

        if (subGenre == null) {
            return false;
        }

        if (!isValidURL(imageURL)) {
            return false;
        }

        return true;
    }

    private boolean isValidURL(String url) {
        if (url == null || url.trim().isEmpty()) return false;

        try {
            new URL(url); // Si no llega a lanzar una excepción, significa que es válida
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", yearLaunch=" + yearLaunch +
                ", subGenre=" + subGenre +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
