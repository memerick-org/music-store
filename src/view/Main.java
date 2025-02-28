
package view;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import model.Album;
import model.Library;
import model.Song; 

//test commit
  
public class Main {
    public static void main(String[] args) {
        
        File folder= new File("/Users/galiramirez/Downloads/LA 1/albums");
 
        ArrayList<Album> albumList = readfilesFromFolder(folder);
        if (albumList == null) {
            //System.out.println("Error: albumList is NULL!");
        } else {
            //System.out.println("albumList contains " + albumList.size() + " albums.");
        }

        Library library= new Library(new ArrayList<Album>(albumList));
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
}