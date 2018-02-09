package org.bubulescu.rest;


public class Joke {

    private int id;
    private String joke;

    public Joke(int id, String joke) {
        this.id = id;
        this.joke = joke;
    }

    public int getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }
}
