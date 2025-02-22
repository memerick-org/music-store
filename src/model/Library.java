package model;

import java.util.ArrayList;

public class Library {
    private String name;
    private ArrayList<Album> albums;

    public Library(String name, ArrayList<Album> albums) {
        this.name = name;
        ArrayList<Album> temp = new ArrayList(albums);
        this.albums = temp;
    }
}