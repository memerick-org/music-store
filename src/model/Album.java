package model;

import java.util.ArrayList;

public class Album {
    private String title;
    private String artist;
    private String date;
    private String genre;
    private ArrayList<Song> songs;

    public Album(Album other) {
        this.title = other.title;
        this.artist = other.artist;
        this.date = other.date;
        this.genre = other.genre;
        this.songs = new ArrayList<>();
        for (Song song : other.songs) {
            this.songs.add(new Song(song));
        }
    }

    public Album(String title, String artist, ArrayList<Song> songs, String genre, String date) {
        this.title = title;
        this.artist = artist;
        ArrayList<Song> temp = new ArrayList<Song>(songs);
        this.songs = temp;
        this.genre = genre;
        this.date = date;
    }

    public void addSong(Song song) {
        if (songs.contains(song)) {
            return;
        }
        songs.add(song);
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

    public boolean equals(Album album) {
        if (this.title.equals(album.title)) {
            return true;
        }
        return false;
    }

}