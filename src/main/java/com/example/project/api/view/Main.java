package com.example.project.api.view;

import com.example.project.api.controller.MovieController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // SpringApplication.run(Main.class, args);
        // System.setProperty("java.awt.headless", "false");

        MovieController movieController = new MovieController();
        MovieView movieView = new MovieView(movieController);

    }

}
