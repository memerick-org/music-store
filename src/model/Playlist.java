package model;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private  ArrayList<Song> songs;

    public Playlist(String name, ArrayList<Song> songs) {
        this.name = name;
        ArrayList<Song> temp = new ArrayList(songs);
        this.songs = temp;
    }

    public String getName() {
        return this.name;
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
