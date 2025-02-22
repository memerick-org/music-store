package model;

public class Song {
    private Album album;
    private String title;
    private String artist;
    private boolean favorite;

    public Song(Album album, String title, String artist) {
        this.album = album;
        this.title = title;
        this.artist = artist;
    }
}