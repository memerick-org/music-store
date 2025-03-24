package view;

import model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class MusicStore {

    public Library store;
    private HashMap <String, Album> albumMap; 
    private HashMap <String, Set<Album>> artistAlbumMap;
    private HashMap <String, Song> songMap;
    private HashMap <String, Set<Song>> artistSongMap;
    private User loggedInUser;
    private UserManager userManager;
    private Library userLibrary;
    private LinkedList<Song> recentlyPlayedSongs = new LinkedList<>(); 
    private Map<Song, Integer> playCounts = new HashMap<>(); 
    private List<Song> mostFrequentlyPlayedSongs = new ArrayList<>(); 

    

    public MusicStore() {

        File folder= new File("/Users/galiramirez/Downloads/LA 1/albums");
        readfilesFromFolder(folder);
        store= new Library(albumMap);
        userLibrary = new Library(new HashMap <>());
        userManager= new UserManager(); 

    }
     
    public void readfilesFromFolder(File folder) {
        File[] files = folder.listFiles();
        albumMap = new HashMap<>();
        artistAlbumMap= new HashMap<>();
        songMap= new HashMap<>(); 
        artistSongMap= new HashMap <>(); 
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line = "", title = "", artist = "", genre = "", date = "";
                        

                        if ((line = reader.readLine()) != null) {
                            String[] firstLine = line.split(",");
                            
                            title = firstLine[0].trim().toLowerCase();
                            artist = firstLine[1].trim().toLowerCase();
                            genre = firstLine[2].trim();
                            date = firstLine[3].trim();
                        }
                        Set<Song> songList= new HashSet<>();

                        while ((line = reader.readLine()) != null) {
                            line = line.trim().toLowerCase();
                            if (!line.isEmpty()) {
                                Song song = new Song(title, line, artist);
                                songList.add(song);
                                songMap.put(line, song);

                                if (!artistSongMap.containsKey(artist)){
                                  artistSongMap.put(artist, new HashSet<>() );
                                  
                                }
                                artistSongMap.get(artist).add(song);

                            }
                        }

                        Album album = new Album(title, artist, songList, genre, date);
                        System.out.println(album.toString());
                        albumMap.put(title, album);

                        if (!artistAlbumMap.containsKey(artist)){
                            artistAlbumMap.put(artist, new HashSet<>()); 
                        }
                        artistAlbumMap.get(artist).add(album);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    
                }
            }
        }
        
    }

    public void mainLoop() {
        Scanner scnr = new Scanner(System.in);
        while (loggedInUser== null) {
            System.out.println("1. Log in\n2. Sign up\nEnter option:");
            String choice = scnr.nextLine().trim();
            if (choice.equals("1")){
                logIn(scnr);
            }else if (choice.equals("2")){
                signUp(scnr); 
            } else {
                System.out.println("Invalid option.");
            }
        }
        while(true){

            System.out.println("Enter command (search, add, remove, get, playlist, favorite, rate, exit, logout, play):");
            String input = scnr.nextLine().trim();

            switch (input) {
                case "search":
                    search(scnr);
                    break;
                case "add":
                    add(scnr);
                    break;
                case "remove":
                    remove(scnr);
                    break;
                case "get": 
                    get(scnr);
                    break;
                case "playlist":
                    playlist(scnr);
                    break;
                case "play":
                    play(scnr);
                    break;
                case "shuffle":
                    shuffle(scnr);
                    break;
                case "favorite":
                    favorite(scnr);
                    break;
                case "rate":
                    rate(scnr);
                    break;
                case "logout":
                    saveUserLibrary();  
                    System.out.println("You have been logged out.");
                    loggedInUser = null;
                    return;
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
                    System.out.println("- play  : Mark a song as playing:");
                    System.out.println("- favorite  : Mark a song as a favorite.");
                    System.out.println("- rate      : Rate a song and provide a score.");
                    System.out.println("- exit      : Exit the program.");
                    System.out.println("- logout      : Logout of program");
                    System.out.println("- help      : Display this help message.");

                    break;
                default:
                    System.out.println("Unknown Command!");
            }
        }
    }
    private void logIn (Scanner scnr){
        System.out.println("Username: ");
        String username= scnr.nextLine().trim(); 
        System.out.println("Password: ");
        String password= scnr.nextLine().trim();

        if (userManager.authenticateUser(username, password)) {
            loggedInUser = userManager.getUser(username);
            loadUserLibrary(username);                   
            System.out.println("Login Successful");
        } else {
            System.out.println("Invalid username or password");
        }
        }
        
    private void signUp(Scanner scnr){
        System.out.println("Choose a username:");
        String username= scnr.nextLine().trim();
        System.out.println("Choose password");
        String password= scnr.nextLine().trim();

        if (userManager.createUser(username, password)){
            loggedInUser= new User (username, password);
            userLibrary= new Library (new HashMap<>());
            System.out.println("Account created Successfully");
        }else {
            System.out.println("Username already taken");
        }
    }
    private void loadUserLibrary(String username){
        File file = new File ( "user_library.txt");
        userLibrary= new Library(file,username, albumMap);
        if (file.exists()){
            userLibrary= new Library (file, username,albumMap);
        }else {
            userLibrary= new Library( new HashMap<>()); 
        }
    }
    private void saveUserLibrary() {
        if (loggedInUser == null) {
            System.out.println("DEBUG: No user logged in. Cannot save library.");
            return;
        }
        String filename = "user_library.txt"; 
        System.out.println("Saving user library for " + loggedInUser.getUsername() + " to: " + filename);
        userLibrary.saveLibrary(filename, loggedInUser.getUsername());
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
            titleMatches.forEach(item -> System.out.println(item.toString() + "\n"));
        }
    
        if (!artistMatches.isEmpty()) {
            System.out.println("Results with matching **Artist**:");
            artistMatches.forEach(item -> System.out.println(item.toString() + "\n"));
        }
    
        if (type.equals("song") && !titleMatches.isEmpty()) {
            System.out.print("Would you like to view the album details for one of the songs? (yes/no): ");
            String response = scnr.nextLine().trim().toLowerCase();
    
            if (response.equals("yes")) {
                Song selectedSong;
                if (titleMatches.size() == 1) {
                    selectedSong = (Song) titleMatches.get(0);
                } else {
                    System.out.print("Enter the number of the song you'd like to see the album for (1-" + titleMatches.size() + "): ");
                    int choice = Integer.parseInt(scnr.nextLine().trim()) - 1;
                    if (choice < 0 || choice >= titleMatches.size()) {
                        System.out.println("Invalid selection.");
                        return;
                    }
                    selectedSong = (Song) titleMatches.get(choice);
                }
    
                String albumTitle = selectedSong.getAlbum();
    
                Album foundAlbum = null;
                for (Album album : userLibrary.getAllAlbums()) {
                    if (album.getTitle().equalsIgnoreCase(albumTitle)) {
                        foundAlbum = album;
                        break;
                    }
                }
    
                if (foundAlbum != null) {
                    System.out.println("Album is in your library.");
                    System.out.println(foundAlbum);
                } else {
                    System.out.println("Album not found in your library.");
                }
            }
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
            String songName = scnr.nextLine().trim().toLowerCase();
            
            if(songMap.containsKey(songName)){
                userLibrary.addSong(songMap.get(songName));
                System.out.println("Song added to your library!");
                return;

            }
            System.out.println("Song not found in store.");
            
        } else if (type.equals("album")) {
            System.out.println("Enter the album name:");

            String albumName = scnr.nextLine().trim().toLowerCase();
                       
            if(albumMap.containsKey(albumName)){
                userLibrary.addAlbum(albumMap.get(albumName));
                System.out.print("Album added to your library!\n");
                return;
            }
            System.out.println("Album not found in store.");
        }
    }


    public void get(Scanner scnr) {
        System.out.println("Please enter what you would like to get from the User Library:\n (song, artist, album, playlist, favorites, recent, frequent) ");
        String input = scnr.nextLine().trim().toLowerCase();
    
        switch(input) {
            case "song":
            System.out.println("How would you like to sort the songs?");
            System.out.println("1. By Title");
            System.out.println("2. By Artist");
            System.out.println("3. By Rating");
            System.out.print("Enter your choice: ");
            String sortChoice = scnr.nextLine().trim();
        
            List<Song> sortedSongs;
            switch (sortChoice) {
                case "1":
                    sortedSongs = userLibrary.getAllSongsSortedBy("title");
                    break;
                case "2":
                    sortedSongs = userLibrary.getAllSongsSortedBy("artist");
                    break;
                case "3":
                    sortedSongs = userLibrary.getAllSongsSortedBy("rating");
                    break;
                default:
                    System.out.println("Invalid choice. Showing unsorted songs.");
                    sortedSongs = new ArrayList<>(userLibrary.getAllSongs());
                    break;
            }
        
            for (Song song : sortedSongs) {
                System.out.println(song + "\n");
            }
            break;
        
            case "artist":
                Set<String> artists = userLibrary.getAllArtists();
                for (String artist : artists) {
                    System.out.println(artist + "\n");
                }
                break;
            case "album":
                Collection<Album> albums = userLibrary.getAllAlbums();
                for (Album album : albums) {
                    System.out.println(album.toString() + "\n");
                }
                break;
            case "playlist":
                Set<Playlist> playlists = userLibrary.getAllPlaylists();
                if (playlists.isEmpty()) {
                    System.out.println("No playlists found");
                } else {
                    for (Playlist playlist : playlists) {
                        System.out.println(playlist.toString() + "\n");
                    }
                }
                break;
            case "favorites":
                Set<Song> favorites = userLibrary.getAllFavoriteSongs();
                if (favorites.isEmpty()) {
                    System.out.println("No favorites found.");
                } else {
                    for (Song favorite : favorites) {
                        System.out.println(favorite.toString());
                    }
                }
                break;
            case "recent":
                List<Song> recent = userLibrary.getRecentlyPlayedSongs();
                if (recent.isEmpty()) {
                    System.out.println("No recently played songs.");
                } else {
                    System.out.println("Recently Played Songs:");
                    for (Song song : recent) {
                        System.out.println(song.getTitle() + " - " + song.getArtist());
                    }
                }
                break;
            case "frequent":
                List<Song> frequent = userLibrary.getMostFrequentlyPlayedSongs();
                if (frequent.isEmpty()) {
                    System.out.println("No frequently played songs.");
                } else {
                    System.out.println("Most Frequently Played Songs:");
                    for (Song song : frequent) {
                        int count = userLibrary.getPlayCount(song);
                        System.out.println(song.getTitle() + " - " + song.getArtist() + " (Played " + count + " times)");
                    }
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    

    public void playlist(Scanner scnr) {

        
        System.out.println("Would you like to make a playlist or edit a playlist?");
        String input= scnr.nextLine().trim().toLowerCase();

        switch (input){
            case "make":
            makePlaylist(scnr);
            break;
            case "edit":
            editPlaylist(scnr);
            break;
            default:
                System.out.println("Error. Unrecognized command.");
        }
    }
    public void makePlaylist(Scanner scnr) {
        System.out.println("Enter a name for your new playlist:");
        String playlistName = scnr.nextLine().trim();
    
        if (playlistName.isEmpty()) {
            System.out.println("Invalid playlist name. Returning to main menu.");
            return;
        }
    
        Playlist newPlaylist = new Playlist(playlistName, new ArrayList<>());
        userLibrary.addPlaylist(newPlaylist);
        saveUserLibrary();
        System.out.println("Playlist '" + playlistName + "' created successfully!");
    }
    

public void editPlaylist(Scanner scnr) {
    Set<Playlist> allPlaylists = userLibrary.getAllPlaylists();
    System.out.println(allPlaylists);

    if (allPlaylists.isEmpty()) {
        System.out.println("You have no playlists to edit.");
        return;
    }

    System.out.println("Your Playlists:");
    int index = 1;
    ArrayList<Playlist> playlistList = new ArrayList<>(allPlaylists);
    
    for (Playlist p : playlistList) {
        System.out.println(index + ". " + p.getName());
        index++;
    }

    System.out.println("Enter the number of the playlist you want to edit:");
    int choice;
    
    try {
        choice = Integer.parseInt(scnr.nextLine().trim());
    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Returning to main menu.");
        return;
    }

    if (choice < 1 || choice > playlistList.size()) {
        System.out.println("Invalid selection. Returning to main menu.");
        return;
    }

    Playlist selectedPlaylist = playlistList.get(choice - 1);

    System.out.println("\nPlaylist: " + selectedPlaylist.getName());
    if (selectedPlaylist.getSongs().isEmpty()) {
        System.out.println("This playlist is currently empty.");
    } else {
        System.out.println("Songs in this playlist:");
        int songIndex = 1;
        for (Song song : selectedPlaylist.getSongs()) {
            System.out.println(songIndex + ". " + song.getTitle() + " - " + song.getArtist());
            songIndex++;
        }
    }

    System.out.println("Would you like to add or remove a song? (add/remove)");
    String action = scnr.nextLine().trim().toLowerCase();

    switch (action) {
        case "add":
            System.out.println("Enter song title:");
            String title = scnr.nextLine().trim().toLowerCase();
            ArrayList<Song> matchingSongs= searchSongByName(title);
            if (matchingSongs.isEmpty()){
                System.out.println("Error: No matching songs found in the Music Store.");
                return;
            }
            System.out.println("Matching songs found:");
            for (int i = 0; i < matchingSongs.size(); i++){
                Song song = matchingSongs.get(i);
                System.out.println((i+1)+"."+ song.getTitle()+ " - "+ song.getArtist()+ "(Album: "+ song.getAlbum()+ ")");
            }
            System.out.println("Enter the number of the song you want to add: ");
            int songChoice;
            try{
                songChoice= Integer.parseInt(scnr.nextLine().trim());
            }catch (NumberFormatException e) { 
                System.out.println("Invalid input. Returning to main menu.");
                return;
            }
            if (songChoice < 1 || songChoice> matchingSongs.size()){
                System.out.println("Invalid selection. Returning to main menu.");
                return;
            }   
            Song selectedSong= matchingSongs.get(songChoice-1);
            selectedPlaylist.addSong(selectedSong);
            System.out.println("Added " + selectedSong.getTitle()+ " by "+ selectedSong.getArtist()+ "(Album: "+ selectedSong.getAlbum()+ ") to the playlist.");
            break;
            
        case "remove":
            if (selectedPlaylist.getSongs().isEmpty()) {
                System.out.println("This playlist is empty. Nothing to remove.");
                return;
            }

            System.out.println("Enter the number of the song you want to remove:");
            int removeChoice;
            
            try {
                removeChoice = Integer.parseInt(scnr.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Returning to main menu.");
                return;
            }

            if (removeChoice < 1 || removeChoice > selectedPlaylist.getSongs().size()) {
                System.out.println("Invalid selection. Returning to main menu.");
                return;
            }

            Song removedSong = selectedPlaylist.getSongs().get(removeChoice - 1);
            selectedPlaylist.removeSong(removedSong.getTitle());
            System.out.println("Removed '" + removedSong.getTitle() + "' from playlist.");
            break;

        default:
            System.out.println("Invalid action. Returning to main menu.");
    }
}


    public void favorite(Scanner scnr) {
        System.out.println("Enter the name of the song to favorite:");
        String title = scnr.nextLine().trim();
        ArrayList<Song> matchingSongs = searchSongByName(title);

        if (matchingSongs.isEmpty()) {
            System.out.println("Error: No matching songs found in the Music Store.");
            return;
        }

        System.out.println("Matching songs found:");
        for (int i = 0; i < matchingSongs.size(); i++) {
            Song song = matchingSongs.get(i);
            System.out.println((i + 1) + ". " + song.getTitle()+ " - " + song.getArtist() + " (Album: " + song.getAlbum() + ")");
        }

        System.out.println("Enter the number of the song you want to add: ");
        int songChoice;

        try {
            songChoice = Integer.parseInt(scnr.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return;
        }

        if (songChoice < 1 || songChoice > matchingSongs.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }

        Song selectedSong = matchingSongs.get(songChoice - 1);
        
        userLibrary.addFavoriteSong(selectedSong);
        saveUserLibrary();
        System.out.println("Added '" + selectedSong.getTitle() + "' by " + selectedSong.getArtist() + " (Album: " + selectedSong.getAlbum() + ") to the favorites.");
        
    }

    public void rate(Scanner scnr) {
        System.out.println("Please enter the name of the song you wish to rate:");
        String title = scnr.nextLine().trim().toLowerCase();
        
        ArrayList<Song> matchingSongs = searchSongByName(title);
        
        if (matchingSongs.isEmpty()) {
            System.out.println("Error: No matching songs found in the Music Store.");
            return;
        }
    
        System.out.println("Matching songs found:");
        for (int i = 0; i < matchingSongs.size(); i++) {
            Song song = matchingSongs.get(i);
            System.out.println((i + 1) + ". " + song.getTitle() + " - " + song.getArtist() + " (Album: " + song.getAlbum() + ")");
        }
    
        System.out.println("Enter the number of the song you wish to rate:");
        int songChoice;
        
        try {
            songChoice = Integer.parseInt(scnr.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return;
        }
        
        if (songChoice < 1 || songChoice > matchingSongs.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }
    
        
        Song selectedSong = matchingSongs.get(songChoice - 1);
    
        System.out.print("Please enter your rating (1-5): ");
        int rating;
        
        try {
            rating = Integer.parseInt(scnr.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating input. Returning to main menu.");
            return;
        }
    
        if (rating < 1 || rating > 5) {
            System.out.println("Error: Rating must be between 1 and 5. Returning to main menu.");
            return;
        }
    
        selectedSong.rateSong(rating);
        System.out.println("Successfully rated \"" + selectedSong.getTitle() + "\" with a " + rating + "!");
    
        if (rating == 5) {
            userLibrary.addFavoriteSong(selectedSong);
            System.out.println("\"" + selectedSong.getTitle() + "\" has been added to your favorites!");
        }
    }
    

    public ArrayList<Song> searchSongByName(String input) {
        input= input.toLowerCase();
        ArrayList<Song> result= new ArrayList<>();
        for (String key: songMap.keySet()){
            if (key.equalsIgnoreCase((input))){
                result.add(songMap.get(key));

            }
        }
        return result; 
    }
        

    public ArrayList<Song> searchSongByArtist(String input) {
        input = input.toLowerCase();
        if (artistSongMap.containsKey(input)){
            return new ArrayList<> (artistSongMap.get(input));

        }
        return new ArrayList<>();
       
    }

    public ArrayList<Album> searchAlbumByName(String input) {
        input= input.toLowerCase();
        ArrayList <Album> result= new ArrayList<>(); 
        if (albumMap.containsKey(input)){
            Album foundAlbum= albumMap.get(input);
            result.add(foundAlbum);
        }
        

        return result;
    }

    /* Warning: Only Returns Shallow Copy */

    public ArrayList<Album> searchAlbumByArtist(String input) {
       input= input.toLowerCase();
       if (artistAlbumMap.containsKey(input)){
        return new ArrayList<>(artistAlbumMap.get(input));
       }

        return new ArrayList<>();
    }
    public void play(Scanner scnr) {
        System.out.println("Enter the name of the song you want to play:");
        String title = scnr.nextLine().trim().toLowerCase();
    

        ArrayList<Song> matchingSongs = searchSongByName(title);
    
        if (matchingSongs.isEmpty()) {
            System.out.println("Error: No matching songs found in the Music Store.");
            return;
        }
    

        System.out.println("Matching songs found:");
        for (int i = 0; i < matchingSongs.size(); i++) {
            Song song = matchingSongs.get(i);
            System.out.println((i + 1) + ". " + song.getTitle() + " - " + song.getArtist() + " (Album: " + song.getAlbum() + ")");
        }
    
        System.out.println("Enter the number of the song you want to play:");
        int songChoice;
        try {
            songChoice = Integer.parseInt(scnr.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return;
        }
    
        if (songChoice < 1 || songChoice > matchingSongs.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }
    
        Song selectedSong = matchingSongs.get(songChoice - 1);
    
        userLibrary.playSong(selectedSong); 
        saveUserLibrary();                  
    
        System.out.println("Now playing: " + selectedSong.getTitle() + " by " + selectedSong.getArtist()
                + " (Album: " + selectedSong.getAlbum() + ")");
    }

    public void remove(Scanner scnr) {
        System.out.println("Do you want to remove a 'song' or an 'album'?");
        String option = scnr.nextLine().trim().toLowerCase();
    
        if (option.equals("song")) {
            System.out.print("Enter song title: ");
            String title = scnr.nextLine().trim();
            System.out.print("Enter artist: ");
            String artist = scnr.nextLine().trim();
            userLibrary.removeSong(title, artist);
        } else if (option.equals("album")) {
            System.out.print("Enter album title: ");
            String albumTitle = scnr.nextLine().trim();
            userLibrary.removeAlbum(albumTitle);
        } else {
            System.out.println("Invalid option.");
        }
    }
    public void shuffle(Scanner scnr) {
        System.out.println("Do you want to shuffle the entire library or a playlist?");
        String target = scnr.nextLine().trim().toLowerCase();
    
        if (target.equals("library")) {
            List<Song> shuffledSongs = userLibrary.getShuffledLibrarySongs();
            System.out.println("Shuffled Library Songs:");
            for (Song song : shuffledSongs) {
                System.out.println(song);
            }
    
        } else if (target.equals("playlist")) {
            Set<Playlist> playlists = userLibrary.getAllPlaylists();
            if (playlists.isEmpty()) {
                System.out.println("No playlists available.");
                return;
            }
    
            System.out.println("Available Playlists:");
            List<Playlist> playlistList = new ArrayList<>(playlists);
            for (int i = 0; i < playlistList.size(); i++) {
                System.out.println((i + 1) + ". " + playlistList.get(i).getName());
            }
    
            System.out.print("Enter playlist number to shuffle: ");
            int choice = Integer.parseInt(scnr.nextLine()) - 1;
    
            if (choice >= 0 && choice < playlistList.size()) {
                Playlist selected = playlistList.get(choice);
                selected.shuffleSongs();
                System.out.println("Playlist '" + selected.getName() + "' shuffled:");
                for (Song song : selected.getSongs()) {
                    System.out.println(song);
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }
    
    
    


}