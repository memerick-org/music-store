package view;

import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MusicStore {

    /* TEMPORARY, REPLACE WITH LIBRARY */
    Library store = new Library(new ArrayList<>());
    Library user = new Library(new ArrayList<>());
    /* TEMPORARY, REPLACE WITH LIBRARY */

    public void mainLoop() {
        Scanner scnr = new Scanner(System.in);
        while (true) {
            System.out.println("Enter desired command. Type 'exit' to quit.");
            String input = scnr.nextLine();

            switch (input) {
                case "search":
                    // TODO: Search Songs or Albums
                    break;
                case "add":
                    // TODO: Add song or album to user library
                    break;
                case "get": 
                    // TODO: Get list from user library of
                    // SONGS
                    // ARTISTS
                    // ALBUMS
                    // PLAYLISTS
                    // FAVORITES
                    break;
                case "playlist":
                    // TODO: Create Playlist
                    // TODO: Add or Remove songs from playlist
                    break;
                case "favorite":
                    favorite();
                    break;
                case "rate":
                    // TODO: Song rating
                    break;
                case "exit":
                    System.out.println("Exiting Program!");
                    return;
                case "help":
                    System.out.println("Available Commands:");
                    System.out.println("- search    : Search for songs or albums in the database.");
                    System.out.println("- add       : Add a song or album to your personal library.");
                    System.out.println("- get       : Retrieve lists of items from your library");
                    System.out.println("- playlist  : Manage your playlists:");
                    System.out.println("- favorite  : Mark a song as a favorite.");
                    System.out.println("- rate      : Rate a song and provide a score.");
                    System.out.println("- exit      : Exit the program.");
                    System.out.println("- help      : Display this help message.");
                default:
                    System.out.println("Unknown Command!");
            }
        }
    }

    public void search() {

    }

    public void add() {

    }

    public void get() {

    }

    public void playlist() {

    }

    public void favorite() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the name of the song to favorite");
        String input = scnr.nextLine();
        ArrayList<Song> searchResults = searchSongByName(input);
        for (Song song : searchResults) {
            song.setFavorite(true);
        }
        scnr.close();
    }

    public void rate() {

    }

    public ArrayList<Song> searchSongByName(String input) {
        ArrayList<Song> results = new ArrayList<>();
        ArrayList<Song> list = store.getAllSongs();

        for (Song song : list) {
            if (song.getTitle() ==  input) {
                results.add(song);
            }
        }

        return results;
    }

    public ArrayList<Song> searchSongByArtist(String input) {
        ArrayList<Song> results = new ArrayList<>();
        ArrayList<Song> list = store.getAllSongs();

        for (Song song : list) {
            if (song.getArtist() == input) {
                results.add(song);
            }
        }

        return results;
    }

    /* Warning: Only Returns Shallow Copy */

    public ArrayList<Album> searchAlbumByName(String input) {
        ArrayList<Album> results = new ArrayList<>();
        ArrayList<Album> list = store.getAllAlbums();

        for (Album album : list) {
            if (album.getTitle() == input) {
                results.add(album);
            }
        }

        return results;
    }

    /* Warning: Only Returns Shallow Copy */

    public ArrayList<Album> searchAlbumByArtist(String input) {
        ArrayList<Album> results = new ArrayList<>();
        ArrayList<Album> list = store.getAllAlbums();

        for (Album album : list) {
            if (album.getArtist() == input) {
                results.add(album);
            }
        }

        return results;
    }
}