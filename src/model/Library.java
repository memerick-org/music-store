package model;

import java.util.ArrayList;

public class Library {
    private String name;
    private ArrayList<Album> albums;
    private ArrayList<Playlist> playlist;

    public Library(String name, ArrayList<Album> albums) {
        this.name = name;
        ArrayList<Album> temp = new ArrayList(albums);
        this.albums = temp;
        this.playlist = null;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> allSongs = new ArrayList<Song>();
        for (Album temp : albums) {
            allSongs.addAll(temp.getSongs());
        }
        return allSongs;
    }
}