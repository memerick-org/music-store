package view;

import model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicStore {
    private Library store;
    private Library user;

    public MusicStore() {
        File folder= new File("albums/");
        ArrayList<Album> albumList = readfilesFromFolder(folder);
        store = new Library(new ArrayList<Album>(albumList));
        user = new Library(new ArrayList<Album>());
    }
     
    public static ArrayList<Album> readfilesFromFolder(File folder) {
        File[] files = folder.listFiles();
        ArrayList<Album> albumList = new ArrayList<>();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line = "", title = "", artist = "", genre = "", date = "";
                        ArrayList<Song> songList = new ArrayList<>();

                        if ((line = reader.readLine()) != null) {
                            line = line.trim();
                            String[] firstLine = line.split(",");
                            
                            title = firstLine[0].trim();
                            artist = firstLine[1].trim();
                            genre = firstLine[2].trim();
                            date = firstLine[3].trim();
                        }

                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (!line.isEmpty()) {
                                Song song = new Song(null, line, artist);
                                songList.add(song);
                            }
                        }

                        Album album = new Album(title, artist, songList, genre, date);
                        System.out.println(album.toString());
                        albumList.add(album);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return albumList;
    }

    public void mainLoop() {
        Scanner scnr = new Scanner(System.in);
        while (true) {
            System.out.println("Enter desired command. Type 'exit' to quit.");
            String input = scnr.nextLine().trim();

            switch (input) {
                case "search":
                    search(scnr);
                    break;
                case "add":
                    add(scnr);
                    break;
                case "get": 
                    get(scnr);
                    break;
                case "playlist":
                    playlist(scnr);
                    break;
                case "favorite":
                    favorite(scnr);
                    break;
                case "rate":
                    rate(scnr);
                    break;
                case "exit":
                    System.out.println("Exiting Program!");
                    scnr.close();
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
                    break;
                default:
                    System.out.println("Unknown Command!");
            }
        }
    }

    public void search(Scanner scnr) {
        System.out.println("Would you like to search for a 'song' or 'album'?");
        String input = scnr.nextLine().trim().toLowerCase();

        switch (input) {
            case "song":
                searchItem(scnr, "song");
                break;
            case "album":
                searchItem(scnr, "album");
                break;
            default:
                System.out.println("Error. Unrecognized command.");
        }
    }

    private void searchItem(Scanner scnr, String type) {
        System.out.println("Enter the title or artist of the " + type + " you are looking for:");
        String input = scnr.nextLine().trim();

        ArrayList<?> titleMatches = type.equals("song") ? searchSongByName(input) : searchAlbumByName(input);
        ArrayList<?> artistMatches = type.equals("song") ? searchSongByArtist(input) : searchAlbumByArtist(input);

        if (titleMatches.isEmpty() && artistMatches.isEmpty()) {
            System.out.println("No exact matches found for the " + type + "!");
            return;
        }

        if (!titleMatches.isEmpty()) {
            System.out.println("Results with matching **Title**:");
            titleMatches.forEach(item -> System.out.println(item.toString()));
        }

        if (!artistMatches.isEmpty()) {
            System.out.println("Results with matching **Artist**:");
            artistMatches.forEach(item -> System.out.println(item.toString()));
        }
    }


    public void add(Scanner scnr) {
        System.out.println("Would you like to add a 'song' or 'album' to your Library?");
        String input = scnr.nextLine().trim().toLowerCase();

        switch (input) {
            case "song":
                addItem(scnr, "song");
                break;
            case "album":
                addItem(scnr, "album");
                break;
            default:
                System.out.println("Error. Unrecognized command.");
        }
    }

    public void addItem(Scanner scnr, String type) {

        if (type.equals("song")) {
            System.out.println("Enter the song name:");
            String songName = scnr.nextLine().trim();
            
            // Search for the song in the store
            for (Song song : store.getAllSongs()) {
                if (song.getTitle().equalsIgnoreCase(songName)) {
                    Album album = (searchAlbumByName(song.getAlbum())).get(0);
                    album.addSong(song);
                    user.addAlbum(album);
                    System.out.println("Song added to your library!");
                    return;
                }
            }
            System.out.println("Song not found in store.");
            
        } else if (type.equals("album")) {
            System.out.println("Enter the album name:");
            String albumName = scnr.nextLine().trim();
            
            // Search for the album in the store
            for (Album album : store.getAllAlbumsDeepCopy()) {
                if (album.getTitle().equalsIgnoreCase(albumName)) {
                    user.getAllAlbums().remove(album);
                    user.addAlbum(album);
                    System.out.println("Album added to your library!");
                    return;
                }
            }
            System.out.println("Album not found in store.");
        }
    }


    public void get(Scanner scnr) {
        System.out.println("Please enter what you would like to get from the User Library: ");
        String input = scnr.nextLine().trim();

        switch(input) {
            case "song":
                ArrayList<Song> songs = user.getAllSongs();
                for (Song song : songs) {
                    System.out.println(song.toString() + "\n");
                }
                break;
            case "artist":
                ArrayList<String> artists = user.getAllArtists();
                for (String artist : artists) {
                    System.out.println(artist + "\n");
                }
                break;
            case "album":
                ArrayList<Album> albums = user.getAllAlbums();
                for (Album album : albums) {
                    System.out.println(album.toString() + "\n");
                }
                break;
            case "playlist":
                ArrayList<Playlist> playlists = user.getAllPlaylists();
                for (Playlist playlist : playlists) {
                    System.out.println(playlist.toString() + "\n");
                }
                break;
            case "favorites":
                ArrayList<Song> favorites = user.getAllFavoriteSongs();
                for (Song favorite : favorites) {
                    System.out.println(favorite.toString());
                }
                
        }
    }

    public void playlist(Scanner scnr) {
        // Implement playlist management logic
        
        System.out.println("Would you like to make a playlist or edit a playlist?");
        String input= scnr.nextLine().trim().toLowerCase();

        switch (input){
            case "make":
            makePlaylist(scnr, "make");
            break;
            case "edit":
            editPlaylist(scnr, "edit",  "Unknown Album");
            break;
            default:
                System.out.println("Error. Unrecognized command.");
        }
    }
    public void makePlaylist(Scanner scnr, String type){
        System.out.println("Making PlayList01");
        Playlist newPlaylist= new Playlist(type, new ArrayList<>());
        user.addPlaylist(newPlaylist);


    }
    public void editPlaylist(Scanner scnr, String type, String album){
            System.out.println("Which playlist will you like to edit");
            String input = scnr.nextLine().trim().toLowerCase();
            ArrayList<Playlist> allPlaylists= store.getAllPlaylists(); 
            Playlist selectedPlaylist= null;
            for (Playlist p: allPlaylists){
                if (p.getName().equals(input)){
                    selectedPlaylist= p;
                    break;
                }
            }
            if (selectedPlaylist== null){
                System.out.println("Error: Playlist not found\n");
            }
            System.out.println("Would you like to add or remove a song? ");
            String action= scnr.nextLine().trim().toLowerCase();
            switch (action){
                case "add":
                System.out.println("Enter song title: ");
                String title=scnr.nextLine().trim();
                System.out.println("Enter artist name");
                String artist = scnr.nextLine().trim();
    
                selectedPlaylist.addSong(new Song("Unknown Album",title, artist));
                break;
                case "remove":
                System.out.println("Enter song title: ");
                String removeTitle=scnr.nextLine().trim();
                selectedPlaylist.removeSong(removeTitle);
                break;
        }
    }

    public void favorite(Scanner scnr) {
        System.out.println("Enter the name of the song to favorite:");
        String input = scnr.nextLine().trim();
        ArrayList<Song> searchResults = searchSongByName(input);

        if (searchResults.isEmpty()) {
            System.out.println("Error, nothing found. Returning to main menu.");
            return;
        }

        System.out.println("Matching songs:");
        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println((i + 1) + ". " + searchResults.get(i).getTitle());
        }

        System.out.print("Please enter the number of the song you wish to favorite: ");
        int choice;

        try {
            choice = scnr.nextInt();
            scnr.nextLine().trim();
        } catch (Exception e) {
            System.out.println("Invalid input. Returning to main menu.");
            scnr.nextLine().trim();
            return;
        }

        if (choice < 1 || choice > searchResults.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }

        Song selectedSong = searchResults.get(choice - 1);
        selectedSong.setFavorite(true);
        System.out.println("\"" + selectedSong.getTitle() + "\" has been added to your favorites!");
    }

    public void rate(Scanner scnr) {
        System.out.println("Please enter the name of the song you wish to rate:");
        String input = scnr.nextLine().trim();
        ArrayList<Song> searchResults = searchSongByName(input);
        
        if (searchResults.isEmpty()) {
            System.out.println("Error, nothing found. Returning to main menu.");
            return;
        }

        System.out.println("Matching songs:");
        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println((i + 1) + ". " + searchResults.get(i).getTitle());
        }

        System.out.print("Please enter the number of the song you wish to rate: ");
        int choice;
        
        try {
            choice = scnr.nextInt();
            scnr.nextLine().trim();
        } catch (Exception e) {
            System.out.println("Invalid input. Returning to main menu.");
            scnr.nextLine();
            return;
        }

        if (choice < 1 || choice > searchResults.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }

        Song selectedSong = searchResults.get(choice - 1);
        
        System.out.print("Please enter your rating (1-5): ");
        
        try {
            int rating = scnr.nextInt();
            scnr.nextLine().trim();
            selectedSong.setRating(rating);
            System.out.println("Successfully rated \"" + selectedSong.getTitle() + "\" with a " + rating + "!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + " Returning to main menu.");
        } catch (Exception e) {
            System.out.println("Invalid rating input. Returning to main menu.");
            scnr.nextLine().trim();
        }
    }


    public ArrayList<Song> searchSongByName(String input) {
        ArrayList<Song> results = new ArrayList<>();
        ArrayList<Song> list = store.getAllSongs();

        for (Song song : list) {
            if (song.getTitle().equals(input)) {
                results.add(song);
            }
        }

        return results;
    }

    public ArrayList<Song> searchSongByArtist(String input) {
        ArrayList<Song> results = new ArrayList<>();
        ArrayList<Song> list = store.getAllSongs();

        for (Song song : list) {
            if (song.getArtist().equals(input)) {
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
            if (album.getTitle().equals(input)) {
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
            if (album.getArtist().equals(input)) {
                results.add(album);
            }
        }

        return results;
    }
}