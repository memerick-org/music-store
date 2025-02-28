package model;

import java.util.ArrayList;

public class Album {
    private String title;
    private String artist;
    private String date;
    private String genre;
    private ArrayList<Song> songs;

    public Album(String title, String artist, ArrayList<Song> songs, String genre, String date) {
        this.title = title;
        this.artist = artist;
        ArrayList<Song> temp = new ArrayList<Song>(songs);
        this.songs = temp;
    }

    public String getTitle() {
        return title;
    }


    public String getDate() {
        return date;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }
   

    public ArrayList<Song> getSongs() {
        ArrayList<Song> copy = new ArrayList<Song>(songs);
        return copy;
    }

    public String toString() {
        return "Album: " + title + "\nArtist: " + artist + "\nGenre: " + genre + "\nDate: " + date;
    }

}