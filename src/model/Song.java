package model;

public class Song {
    private String album;
    private String title;
    private String artist;
    private boolean favorite;
    private int rating;

    public Song(String album, String title, String artist) {
        this.album = album;
        this.title = title;
        this.artist = artist;
        this.favorite = false; // unreated 
        this.rating= 0; // unrated
    }
    // setting up the parameters for rating and error handling 
    public void rateSong(int rating){
        if (rating < 1 || rating > 5){
            throw new IllegalArgumentException( "Rating must be between 1 and 5");
        }
        this.rating=rating;
        this.favorite= (rating==5); // rate 5 automatically set to favorite
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isFavorite() {
        return favorite;
    }
    
    public int getRating(){
        return rating;
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