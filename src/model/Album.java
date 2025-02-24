package model;

import java.util.ArrayList;

public class Album {
    private String title;
    private String artist;
    private ArrayList<Song> songs;

    public Album(String title, String artist, ArrayList<Song> songs) {
        this.title = title;
        this.artist = artist;
        ArrayList<Song> temp = new ArrayList<Song>(songs);
        this.songs = songs;
    }
    
}
