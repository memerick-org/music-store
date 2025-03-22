package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Playlist {
    private String name;
    private  List<Song> songs;

    public Playlist(String name, ArrayList<Song> songs) {
        this.name = name;
        this.songs= new ArrayList<>(); 
        for (Song song: songs){
            this.songs.add(new Song(song));
        }
    }

    public String getName() {
        return this.name;
    }
    public List<Song> getSongs(){
        return new ArrayList<>(songs);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist: ").append(name).append("\nSong List:\n").append(songs);
        
        for (Song song : songs) {
            sb.append(song.toString()).append("\n");
        }
        
        return sb.toString();
    }

    public void addSong(Song song ){
        songs.add(new Song(song));
    }
    public void removeSong (String title){
        title= title.trim().toLowerCase();
        Set<Song> toRemove= new HashSet<>(); 
        for (Song song: songs){
            if (song.getTitle().trim().toLowerCase().equals(title)){
                toRemove.add(song);
            }
        }
        songs.removeAll(toRemove);
    }
    public String getSongsAsString() {
        if (songs.isEmpty()) return "No songs";
        StringBuilder sb = new StringBuilder();
        for (Song song : songs) {
            sb.append(song.getTitle()).append(" by ").append(song.getArtist()).append("; ");
        }
        return sb.toString();
    }
    public void shuffleSongs() {
        Collections.shuffle(songs);
    }
    
    
}   

