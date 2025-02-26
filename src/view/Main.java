
package view;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Album; 
  
public class Main {
    public static void main(String[] args) {
        File folder= new File("/Users/galiramirez/Downloads/LA 1/albums");
        readfilesFromFolder(folder);
            
        }
    public static void readfilesFromFolder(File folder){
        ArrayList<String> album_list = new ArrayList <String> ();
        ArrayList<String> song_list = new ArrayList <String> ();
        ArrayList<String> artist_list = new ArrayList <String> ();
        ArrayList<String> date_list = new ArrayList <String> ();
        ArrayList<String>  genre_list = new ArrayList <String> ();


        File [] files= folder.listFiles();
        if (files != null){
            for (File file :files){
                if (file.isFile()){
                    try (BufferedReader reader= new BufferedReader(new FileReader(file))){
                        String line; 
                        ArrayList <String> firstlines= new ArrayList<>();
                        while ((line = reader.readLine())!=null){
                            firstLines.add(line);
                            
                                
                        }
                        
                    }catch( IOException e) {
                        e.printStackTrace();
                    }


                    }



                        }

                    }
                    
                }
            }
        
    

        // TODO - INDEX ENTIRE MUSIC STORE

        // TODO - WHILE LOOP COMMAND PROCESSING
    

    // TODO - COMMAND FUNCTIONS
