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
        this.favorite = false;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isFavorite() {
        return favorite;
    }

    // Setters
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String toString() {
        String favoriteMarker = favorite ? "* " : "";
        return favoriteMarker + "Song: " + title + "\nArtist: " + artist + "\nAlbum: " + album;
    }
}