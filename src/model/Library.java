package model;

import java.util.ArrayList;

public class Library {
    private ArrayList<Album> albums;
    private ArrayList<Playlist> playlist;

    public Library(ArrayList<Album> albums) {
      
        if (albums !=null ){
            this.albums= new ArrayList<>(albums);
        }else{
            this.albums= new ArrayList<>();
        }
        this.playlist = new ArrayList<>();
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> allSongs = new ArrayList<Song>();
        for (Album temp : albums) {
            allSongs.addAll(temp.getSongs());
        }
        return allSongs;

    }

    public ArrayList<String> getAllArtists() {
        ArrayList<String> allArtists = new ArrayList<String>();
        for (Album album : albums) {
            allArtists.add(album.getArtist());
        }
        return allArtists;
    }

    public ArrayList<Album> getAllAlbums() {
        ArrayList<Album> allAlbums = new ArrayList<>(albums);
        return allAlbums;
    }

    public ArrayList<Album> getAllAlbumsDeepCopy() {
        ArrayList<Album> deepCopyAlbums = new ArrayList<>();
        for (Album album : albums) {
            deepCopyAlbums.add(new Album(album)); // Assumes Album has a copy constructor
        }
        return deepCopyAlbums;
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


    public void addAlbum(Album album) {
        if (albums.contains(album)) {

        }
        albums.add(new Album(album));
    }

    public void addSong(Song song) {
        
    }

    public void addPlaylist(Playlist playList){
        playlist.add(playList);
    }
}