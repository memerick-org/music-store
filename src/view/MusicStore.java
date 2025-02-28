package view;

import model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicStore {
    public Library store;
    public Library user;

    public MusicStore() {
        File folder= new File("/Users/galiramirez/Downloads/LA 1/albums");
        ArrayList<Album> albumList = readfilesFromFolder(folder);
        this.store = new Library(new ArrayList<Album>(albumList));
        this.user = new Library(new ArrayList<Album>());
    }
     
    public static ArrayList<Album> readfilesFromFolder(File folder) {
        File[] files = folder.listFiles();
        ArrayList<Album> albumList = new ArrayList<>();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        String title = "";
                        String artist = "";
                        String genre = "";
                        String date = "";
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
            String input = scnr.nextLine();

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
        
    }

    public void get(Scanner scnr) {
        // Implement retrieval logic for user library
    }

    public void playlist(Scanner scnr) {
        // Implement playlist management logic
    }

    public void favorite(Scanner scnr) {
        System.out.println("Enter the name of the song to favorite");
        String input = scnr.nextLine();
        ArrayList<Song> searchResults = searchSongByName(input);
        for (Song song : searchResults) {
            song.setFavorite(true);
        }
    }

    public void rate(Scanner scnr) {
//   public void rateSong(String title, int rating){
//     for (Song song: songs){
//       if (song.getTitle().toLowerCase().equals(title.toLowerCase())){
//         song.rateSong(rating);
//         for (Song s: songs){
//           System.out.print(s);
//         }
//         return;
//         }
//       }
//       throw new IllegalArgumentException("Song not found:" + title);
//     }
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