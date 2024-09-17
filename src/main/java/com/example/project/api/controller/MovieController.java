package com.example.project.api.controller;

import com.example.project.api.model.Movie;

import com.example.project.api.view.MovieView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.FileReader;
import java.util.Map;
import javax.swing.DefaultListModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class MovieController {

    private MovieView movieView;

    public void displayJson( DefaultListModel<String> movieListModel, Map<String, String> moviePosterMap ){
        File jsonFile = new File("movies.json");
        if (jsonFile.exists()) {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(jsonFile)) {
                Object obj = jsonParser.parse(reader);
                JSONArray movieList = (JSONArray) obj;

                for (Object movieObj : movieList) {
                    JSONObject movieJson = (JSONObject) movieObj;

                    String movieName = (String) movieJson.get("name");
                    String movieYear = (String) movieJson.get("year");
                    String posterPath = (String) movieJson.get("poster");

                    String movieTitle = movieName + " (" + movieYear + ")";
                    movieListModel.addElement(movieTitle);
                    moviePosterMap.put(movieTitle, posterPath);
                }
            } catch (IOException | ParseException e) {
                showError("Error reading movies.json!");
            }
        }
    }

    private void showError(String s) {
    }

    public void createJsonIfNotExist(Movie movie) {
        String filePath = "movies.json";
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<Movie> movies = new ArrayList<>();

        try {
            if (!file.exists()) {
                file.createNewFile();
                objectMapper.writeValue(file, movies);
                System.out.println("Created new movies.json file.");
            }
            else{
                movies = objectMapper.readValue(file, new TypeReference<List<Movie>>() {});
            }

            movies.add(movie);
            objectMapper.writeValue(file, movies);
            System.out.println("Movie added to the JSON file successfully.");
            System.out.println("File located at: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("An error occurred while handling the JSON file: " + e.getMessage());
        }
    }

    @PostMapping("/addMovie")
    public void addMovie(@RequestBody Movie newMovie) {
        try {
            // Gelen yeni filmi Movie nesnesine dönüştür
            Movie addedMovie = new Movie(newMovie.getName(), newMovie.getYear(), newMovie.getPoster());

            // JSON dosyasının yolu
            String filePath = "movies.json";
            File file = new File(filePath);

            // JSON işlemleri için ObjectMapper kullanımı
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            List<Movie> movies;

            // Eğer dosya zaten varsa mevcut filmleri oku
            if (file.exists() && file.length() != 0) {
                movies = Arrays.asList(objectMapper.readValue(file, Movie[].class));
                movies = new ArrayList<>(movies); // Arrays.asList sabit boyutlu bir liste döndürdüğü için, yeni eleman eklemek için ArrayList'e çeviriyoruz
            } else {
                // Eğer dosya yoksa veya boşsa, yeni bir film listesi başlat
                movies = new ArrayList<>();
            }

            // Yeni filmi listeye ekle
            movies.add(addedMovie);

            // Güncellenmiş listeyi JSON dosyasına yaz
            objectMapper.writeValue(file, movies);

        } catch (IOException e) {
            // Hata durumunda hata mesajı göster
            e.printStackTrace();
        }
    }
}

