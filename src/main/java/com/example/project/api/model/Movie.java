package com.example.project.api.model;

public class Movie {

        private String name;
        private String year;
        private String poster;

    // Default constructor
    public Movie() {
    }

    public Movie(String name, String year, String poster) {
        this.name = name;
        this.year = year;
        this.poster = poster;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

    }

