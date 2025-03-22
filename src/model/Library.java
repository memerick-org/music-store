package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.Collections;



public class Library {
    private Map<String, Album> albums;
    private Set<Playlist> playlists;
    private Set<Song> favoriteSongs= new HashSet<>(); 
    private Map<String, Song> songMap;
    private LinkedList<Song> recentlyPlayedSongs;
    private Map<Song, Integer> songPlayCount; 
    private List<Song> mostFrequentlyPlayedSongs;


    public Library(File file, String username, Map<String, Album> albumsFromStore) {
        this.albums = new HashMap<>(albumsFromStore);
        this.playlists = new HashSet<>();
        this.favoriteSongs = new HashSet<>();
        this.songMap = new HashMap<>();
        this.recentlyPlayedSongs = new LinkedList<>();
        this.songPlayCount = new HashMap<>();
        this.mostFrequentlyPlayedSongs = new ArrayList<>();
    
        for (Album album : albums.values()) {
            for (Song song : album.getSongs()) {
                String key = song.getTitle().toLowerCase() + "|" + song.getArtist().toLowerCase();
                songMap.put(key, song);
            }
        }   
        loadUserLibrary(file, username);
    }
    
    public Library(Map<String, Album> albums) {
        if (albums != null) {
            this.albums = new HashMap<>(albums);
        } else {
            this.albums = new HashMap<>();
        }
        this.playlists = new HashSet<>();
        this.favoriteSongs = new HashSet<>();
        this.songMap = new HashMap<>();
        this.recentlyPlayedSongs = new LinkedList<>();
        this.songPlayCount = new HashMap<>();
        this.mostFrequentlyPlayedSongs = new ArrayList<>();
    }
    

    public Set<Song> getAllSongs() {
        Set<Song> allSongs = new HashSet<>();
        for (Album album : albums.values()) {
            allSongs.addAll(album.getSongs());
        }
        return allSongs;

    }

    public Set<String> getAllArtists() {
        Set<String> allArtists = new HashSet<String>();
        for (Album album : albums.values()) {
            allArtists.add(album.getArtist());
        }
        return allArtists;
    }

    public Collection<Album> getAllAlbums() {
        return new ArrayList<>(albums.values());
    }

    public List<Album> getAllAlbumsDeepCopy() {
        List<Album> deepCopyAlbums = new ArrayList<>();
        for (Album album : albums.values()) {
            deepCopyAlbums.add(new Album(album)); 
        }
        return new ArrayList<>(albums.values());
    }

    public Set<Playlist> getAllPlaylists() {
        if (playlists == null || playlists.isEmpty()) {
            System.out.println("DEBUG: Playlists are empty.");
            return new HashSet<>();
        }
        System.out.println("DEBUG: Retrieved " + playlists.size() + " playlists.");
        return new HashSet<>(playlists);
    }
    public List<Song> getAllSongsSortedBy(String criteria) {
        List<Song> sorted = new ArrayList<>(getAllSongs()); // Copy list to avoid modifying original
    
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {
                Song a = sorted.get(j);
                Song b = sorted.get(j + 1);
    
                boolean shouldSwap = false;
    
                switch (criteria.toLowerCase()) {
                    case "title":
                        if (a.getTitle().compareToIgnoreCase(b.getTitle()) > 0) {
                            shouldSwap = true;
                        }
                        break;
                    case "artist":
                        if (a.getArtist().compareToIgnoreCase(b.getArtist()) > 0) {
                            shouldSwap = true;
                        }
                        break;
                    case "rating":
                        if (a.getRating() > b.getRating()) {
                            shouldSwap = true;
                        }
                        break;
                    default:
                        System.out.println("Invalid sort criteria.");
                        return sorted;
                }
    
                if (shouldSwap) {
                    // Swap a and b
                    sorted.set(j, b);
                    sorted.set(j + 1, a);
                }
            }
        }
    
        return sorted;
    }
    
    public Set<Song> getAllFavoriteSongs() {
        if (favoriteSongs == null || favoriteSongs.isEmpty()) {
            System.out.println("DEBUG: No favorite songs found.");
            return new HashSet<>();
        }
        System.out.println("DEBUG: Retrieved " + favoriteSongs.size() + " favorite songs.");
        return new HashSet<>(favoriteSongs);
    }



    public void addAlbum(Album album) {
        albums.put(album.getTitle().toLowerCase(),new Album(album));
    }

    public void addSong(Song song) {
        if (song== null) return;
        String albumTitle = song.getAlbum().toLowerCase();
        String songKey= song.getTitle().toLowerCase() + "|" + song.getArtist().toLowerCase();
        songMap.put(songKey, song);
        if (albums.containsKey(albumTitle)) {
            Album album = albums.get(albumTitle);
            Set<Song> updatedSongs = new HashSet<>(album.getSongs());
            updatedSongs.add(song);
            albums.put(albumTitle, new Album(album.getTitle(), song.getArtist(), updatedSongs, album.getGenre(), album.getDate()));
        } else {
            Set<Song> newSongSet = new HashSet<>();
            newSongSet.add(song);
            albums.put(albumTitle, new Album(albumTitle, song.getArtist(), newSongSet, "Unknown Genre", "Unknown Date"));
        }
        songMap.put(songKey,song);
    }
    

    public void addPlaylist(Playlist playlist) {
        if (playlist == null) return;
        
        if (playlists == null) {
            playlists = new HashSet<>(); 
        }
    
        playlists.add(playlist);
        System.out.println("DEBUG: Playlist '" + playlist.getName() + "' added. Total Playlists: " + playlists.size());
    }
    
    public void saveLibrary(String filename, String username) {
        File file = new File(filename);
        List<String> updatedLines = new ArrayList<>();
    
        try {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean skip = false;
    
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("USER: ")) {
                            String userInFile = line.substring(6).trim();
                            skip = userInFile.equalsIgnoreCase(username);
                        }
    
                        if (!skip) {
                            updatedLines.add(line);
                        }
                    }
                }
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write all previous (non-current user) lines
                for (String line : updatedLines) {
                    writer.write(line);
                    writer.newLine();
                }
    
                writer.write("USER: " + username);
                writer.newLine();
    
                writer.write("PLAYLISTS:");
                writer.newLine();
                for (Playlist playlist : playlists) {
                    writer.write(playlist.getName());
                    for (Song song : playlist.getSongs()) {
                        writer.write("," + song.getTitle() + "|" + song.getArtist());
                    }
                    writer.newLine();
                }
    
                writer.write("FAVORITES:");
                writer.newLine();
                for (Song song : favoriteSongs) {
                    writer.write(song.getTitle() + "|" + song.getArtist());
                    writer.newLine();
                }
    
                writer.write("RECENTLY_PLAYED:");
                writer.newLine();
                if (recentlyPlayedSongs != null && !recentlyPlayedSongs.isEmpty()) {
                    for (Song song : recentlyPlayedSongs) {
                        writer.write(song.getTitle() + "|" + song.getArtist());
                        writer.newLine();
                    }
                } else {
                    System.out.println("DEBUG: no recently played songs to save");
                }
    
                writer.write("MOST_FREQUENTLY_PLAYED:");
                writer.newLine();
                if (mostFrequentlyPlayedSongs != null && !mostFrequentlyPlayedSongs.isEmpty()) {
                    for (Song song : mostFrequentlyPlayedSongs) {
                        int count = songPlayCount.getOrDefault(song, 0);
                        writer.write(song.getTitle() + "|" + song.getArtist() + "," + count);
                        writer.newLine();
                    }
                } else {
                    System.out.println("DEBUG: no frequently played songs to save");
                }
    
                System.out.println("DEBUG: Saved library for user: " + username);
            }
    
        } catch (IOException e) {
            System.out.println("Error saving library: " + e.getMessage());
        }
    }
    
    
    
    
    
    private void loadUserLibrary(File file, String username) {
        if (!file.exists()) {
            System.out.println("Library file not found. Creating a new empty library.");
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean userFound = false;
            boolean loadingPlaylists = false;
            boolean loadingFavorites = false;
            boolean loadingRecentlyPlayed = false;
            boolean loadingMostFrequent = false;
    
            playlists = new HashSet<>();
            favoriteSongs = new HashSet<>();
            recentlyPlayedSongs = new LinkedList<>();
            songPlayCount = new HashMap<>();
            mostFrequentlyPlayedSongs = new ArrayList<>();
    
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("USER: ")) {
                    if (userFound) break; 
                    userFound = line.substring(6).trim().equalsIgnoreCase(username);
                    continue;
                }
    
                if (!userFound) continue;
    
                switch (line) {
                    case "PLAYLISTS:":
                        loadingPlaylists = true;
                        loadingFavorites = loadingRecentlyPlayed = loadingMostFrequent = false;
                        continue;
                    case "FAVORITES:":
                        loadingFavorites = true;
                        loadingPlaylists = loadingRecentlyPlayed = loadingMostFrequent = false;
                        continue;
                    case "RECENTLY_PLAYED:":
                        loadingRecentlyPlayed = true;
                        loadingPlaylists = loadingFavorites = loadingMostFrequent = false;
                        continue;
                    case "MOST_FREQUENTLY_PLAYED:":
                        loadingMostFrequent = true;
                        loadingPlaylists = loadingFavorites = loadingRecentlyPlayed = false;
                        continue;
                }
    
                if (loadingPlaylists) {
                    String[] parts = line.split(",");
                    if (parts.length >= 1) {
                        String playlistName = parts[0].trim();
                        Playlist playlist = new Playlist(playlistName, new ArrayList<>());
    
                        for (int i = 1; i < parts.length; i++) {
                            String[] songParts = parts[i].split("\\|");
                            if (songParts.length == 2) {
                                String songTitle = songParts[0].trim();
                                String artist = songParts[1].trim();
                                String key = songTitle.toLowerCase() + "|" + artist.toLowerCase();
                                Song song = songMap.get(key);
                                if (song != null) {
                                    playlist.addSong(song);
                                }
                            }
                        }
                        playlists.add(playlist);
                    }
                } else if (loadingFavorites) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        String title = parts[0].trim();
                        String artist = parts[1].trim();
                        String key = title.toLowerCase() + "|" + artist.toLowerCase();
                        Song song = songMap.get(key);
                        if (song != null) {
                            song.setFavorite(true);
                            favoriteSongs.add(song);
                        }
                    }
                } else if (loadingRecentlyPlayed) {
                    String[] songParts = line.split("\\|");
                    if (songParts.length == 2) {
                        String songTitle = songParts[0].trim();
                        String artist = songParts[1].trim();
                        String key = songTitle.toLowerCase() + "|" + artist.toLowerCase();
                        Song song = songMap.get(key);
                        if (song != null) {
                            recentlyPlayedSongs.add(song);
                        }
                    }
                   
                } else if (loadingMostFrequent) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String[] songParts = parts[0].split("\\|");
                        if (songParts.length == 2) {
                            String songTitle = songParts[0].trim();
                            String artist = songParts[1].trim();
                            int count = Integer.parseInt(parts[1].trim());
                            String key = songTitle.toLowerCase() + "|" + artist.toLowerCase();
                            Song song = songMap.get(key);
                        if (song != null) {
                            songPlayCount.put(song, count);
                            mostFrequentlyPlayedSongs.add(song);
                        }else{
                            System.out.println("DEBUG: Song not found for frequent: " + key); 
                        }
                    }
                    }
                }
                
            }
    
        } catch (IOException e) {
            System.out.println("Error loading library data: " + e.getMessage());
        }
    }
    
    public void addFavoriteSong(Song song) {
        if (song == null) return;
    
        if (favoriteSongs == null) {
            favoriteSongs = new HashSet<>();
        }
    
        String songKey = song.getTitle().toLowerCase() + "|" + song.getArtist().toLowerCase();
    
        if (songMap.containsKey(songKey)) {
            Song favorite = songMap.get(songKey);
            if (favoriteSongs.contains(favorite)) {
                System.out.println("DEBUG: Song '" + favorite.getTitle() + "' is already in favorites.");
                return;
            }
            favorite.setFavorite(true);
            favoriteSongs.add(favorite);
    
            if (albums.containsKey(favorite.getAlbum().toLowerCase())) {
                Album album = albums.get(favorite.getAlbum().toLowerCase());
                Set<Song> updatedSongs = new HashSet<>(album.getSongs());
                updatedSongs.remove(favorite);
                updatedSongs.add(favorite);
                albums.put(favorite.getAlbum().toLowerCase(),
                    new Album(album.getTitle(), album.getArtist(), updatedSongs, album.getGenre(), album.getDate()));
            }
    
            System.out.println("DEBUG: Favorite song '" + favorite.getTitle() + "' added. Total Favorites: " + favoriteSongs.size());
            return;
        }
    
        song.setFavorite(true);
        favoriteSongs.add(song);
        System.out.println("DEBUG: Manually added '" + song.getTitle() + "' to favorites.");
    }

    public void playSong(Song song) {
        if (song == null) {
            System.out.println("Error: Invalid song.");
            return;
        }
    
        System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
    
        // Update play count
        songPlayCount.put(song, songPlayCount.getOrDefault(song, 0) + 1);
    
        // Update recently played songs
        recentlyPlayedSongs.remove(song);  
        recentlyPlayedSongs.addFirst(song); 
        if (recentlyPlayedSongs.size() > 10) {
            recentlyPlayedSongs.removeLast();  
        }
    
        // Update most frequently played songs (loop version)
        List<Map.Entry<Song, Integer>> entryList = new ArrayList<>(songPlayCount.entrySet());
    
        // Sort by descending play count
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    
        // Take top 10 songs
        List<Song> topSongs = new ArrayList<>();
        for (int i = 0; i < Math.min(10, entryList.size()); i++) {
            topSongs.add(entryList.get(i).getKey());
        }
    
        mostFrequentlyPlayedSongs = topSongs;
    }
    
    
    public List<Song> getRecentlyPlayedSongs() {
        return new ArrayList<>(recentlyPlayedSongs);  
    }
    public List<Song> getMostFrequentlyPlayedSongs() {
        List<Map.Entry<Song, Integer>> sortedEntries = new ArrayList<>(songPlayCount.entrySet());
    
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    
        List<Song> mostPlayedSongs = new ArrayList<>();
        int count = 0;
    
        for (Map.Entry<Song, Integer> entry : sortedEntries) {
            if (count >= 10) break;
            mostPlayedSongs.add(entry.getKey());
            count++;
        }
    
        return mostPlayedSongs;
    }
    public int getPlayCount(Song song) {
        return songPlayCount.getOrDefault(song, 0);
    }
    public boolean removeSong(String title, String artist) {
        String key = title.toLowerCase() + "|" + artist.toLowerCase();
        Song song = songMap.get(key);
        if (song == null) {
            System.out.println("Song not found in library.");
            return false;
        }
    
        String albumTitle = song.getAlbum().toLowerCase();
        if (albums.containsKey(albumTitle)) {
            Album album = albums.get(albumTitle);
            Set<Song> updatedSongs = new HashSet<>(album.getSongs());
            updatedSongs.remove(song);
    
            if (updatedSongs.isEmpty()) {
                albums.remove(albumTitle);
            } else {
                albums.put(albumTitle, new Album(album.getTitle(), album.getArtist(), updatedSongs, album.getGenre(), album.getDate()));
            }
        }
    
        songMap.remove(key);
        favoriteSongs.remove(song);
        recentlyPlayedSongs.remove(song);
        songPlayCount.remove(song);
        mostFrequentlyPlayedSongs.remove(song);
    
        for (Playlist playlist : playlists) {
            playlist.getSongs().removeIf(s -> s.equals(song));
        }
    
        System.out.println("Removed song: " + title + " by " + artist);
        return true;
    }
    public boolean removeAlbum(String albumTitle) {
        String key = albumTitle.toLowerCase();
        if (!albums.containsKey(key)) {
            System.out.println("Album not found.");
            return false;
        }
    
        Album album = albums.get(key);
        for (Song song : album.getSongs()) {
            String songKey = song.getTitle().toLowerCase() + "|" + song.getArtist().toLowerCase();
            songMap.remove(songKey);
            favoriteSongs.remove(song);
            recentlyPlayedSongs.remove(song);
            songPlayCount.remove(song);
            mostFrequentlyPlayedSongs.remove(song);
    
            // Remove song from playlists
            for (Playlist playlist : playlists) {
                playlist.getSongs().removeIf(s -> s.equals(song));
            }
        }
    
        albums.remove(key);
        System.out.println("Removed album: " + albumTitle);
        return true;
    }
    public List<Song> getShuffledLibrarySongs() {
        List<Song> songs = new ArrayList<>(getAllSongs());
        Collections.shuffle(songs);
        return songs;
        
    }
        

}

    
    

    
    
    
    
   