package model;

import  java.util.HashSet; 
import java.util.Set; 

public class Album {
    private String title;
    private String artist;
    private String date;
    private String genre;
    private Set<Song> songs;

    public Album(Album other) {
        this.title = other.title;
        this.artist = other.artist;
        this.date = other.date;
        this.genre = other.genre;
        this.songs = new HashSet<>();
        for (Song song : other.songs) {
            this.songs.add(new Song(song));
        }
    }

    public Album(String title, String artist, Set<Song> songs, String genre, String date) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.date = date;
        this.songs = new HashSet<>();
        for (Song song : songs ){
            this .songs.add(new Song (song));
        }
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
   

    public Set<Song> getSongs() {
        Set<Song> copy = new HashSet<>();
        for (Song song: songs){
            copy.add(new Song(song));
        }
        return copy;
    }

    public String toString() {
        return "Album: " + title + "\nArtist: " + artist + "\nGenre: " + genre + "\nDate: " + date;
    }

}