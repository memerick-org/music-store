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

    public ArrayList<Album> getAllAlbums() {
        ArrayList<Album> allAlbums = new ArrayList<>(albums);
        return allAlbums;
    }

    public ArrayList<Playlist> getAllPlaylists() {
        if (playlist == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(playlist);
    }

    public ArrayList<Song> getAllFavoriteSongs() {
        ArrayList<Song> allFavoriteSongs = new ArrayList<Song>();
        for (Album temp : albums) {
            for (Song song : temp.getSongs()) {
                if (song.isFavorite()) {
                    allFavoriteSongs.add(song);
                }
            }
        }
        return allFavoriteSongs;
    }
}