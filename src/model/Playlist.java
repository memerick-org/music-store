package model;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private ArrayList<Song> songs;

    public Playlist(String name, ArrayList<Song> songs) {
        this.name = name;
        ArrayList<Song> temp = new ArrayList(songs);
        this.songs = temp;
    }
}
