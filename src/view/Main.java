
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
        Library library= new Library( new ArrayList<>());
 
        ArrayList<Album> albumList = readfilesFromFolder(folder);
        if (albumList == null) {
            //System.out.println("Error: albumList is NULL!");
        } else {
            //System.out.println("albumList contains " + albumList.size() + " albums.");
        }
        
                // Add albums to library
                for (Album album : albumList) {
                library.addAlbum(album);
                }
        
                // Print library contents for verification
                //System.out.println("Library contains " + library.getAlbums().size() + " albums.");
            }
     
    public static ArrayList<Album> readfilesFromFolder(File folder){
        File [] files= folder.listFiles();
        ArrayList<Album> albumList= new ArrayList<>();
        if (files != null){
            
            for (File file :files){
                if (file.isFile() && file.getName().endsWith(".txt")){
                    try (BufferedReader reader= new BufferedReader(new FileReader(file))){
                        
                        String line; 
                        String title= ""; 
                        String artist = "";
                        String genre= ""; 
                        String date = "";
                        ArrayList<Song> songList = new ArrayList<>();
                        //ArrayList<Album> albumList= new ArrayList<>();
                        if( (line= reader.readLine())!= null){
                           // //System.out.println("Raw first line in file " + file.getName() + ": [" + line + "]");

                            line= line.trim();
                            String [] firstLine= line.split(",");
                            ////System.out.println("Parsed firstLine array: " + Arrays.toString(firstLine)+ firstLine.length);
                            title= firstLine[0].trim();
                            artist= firstLine[1].trim();
                            genre= firstLine[2].trim();
                           date= firstLine[3].trim();
                            //System.out.println("Firstline"+ Arrays.toString(firstLine));
                           //System.out.println("Album Title: "+ title);
                          //System.out.println("Artist Name: "+ artist);
                           //System.out.println("Genre "+ genre);
                         //System.out.println("Date "+ date);
                            
                        }
                       
                        while ((line = reader.readLine()) !=null){
                               line= line.trim();
                               if (!line.isEmpty()){
                                    ////System.out.println("adding song " + line );
                                    ////System.out.println("Processing line: [" + line + "] | Length: " + line.length());

                                    Song songs = new Song( null, line.trim(), artist);
                                    songList.add(songs);
                       
                            }
                        }    
                        Album album= new Album(title, artist, songList, genre, date);
                        
                        albumList.add(album);

                        ////System.out.println("AlbumList: "+ albumList.size());
                    }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    
                    
                }
            }
            return albumList;
        }  
    }