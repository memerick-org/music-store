package model;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private  ArrayList<Song> songs;

    public Playlist(String name, ArrayList<Song> songs) {
        this.name = name;
        ArrayList<Song> temp = new ArrayList<Song>(songs);
        this.songs = temp;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist: ").append(name).append("\nSong List:\n");
        
        for (Song song : songs) {
            sb.append(song.toString()).append("\n");
        }
        
        return sb.toString();
    }

    public void addSong(Song song ){
        songs.add(song);
    }
    public void removeSong (String title){
        title= title.trim().toLowerCase();
        for (Song song: songs){
            if (song.getTitle().equals(title)){
                songs.remove(song);
            }
        }
    }
}
