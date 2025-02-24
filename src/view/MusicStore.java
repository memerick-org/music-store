package view;

import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MusicStore {

    /* TEMPORARY, REPLACE WITH LIBRARY */
    Library store = new Library("store", new ArrayList<>());
    Library user = new Library("user", new ArrayList<>());
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
        ArrayList<Song> searchResults = searchByName(input);
        for (Song song : searchResults) {
            song.setFavorite(true);
        }
        scnr.close();
    }

    public void rate() {

    }

    public ArrayList<Song> searchByName(String name) {
        ArrayList<Song> results = new ArrayList<>();
        ArrayList<Song> list = store.getAllSongs();

        for (Song song : list) {
            if (song.getTitle() ==  name) {
                results.add(song);
            }
        }

        return results;
    }
}