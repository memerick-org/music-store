package model;

import java.util.ArrayList;

public class Rating {
  private ArrayList<Song> songs;
  
  public Rating(){
    this.songs= new ArrayList<>();
  }
  public void addSong (Album album, String title, String artist){
    songs.add(new Song( album, title, artist));
  }
  public void rateSong(String title, int rating){
    for (Song song: songs){
      if (song.getTitle().toLowerCase().equals(title.toLowerCase())){
        song.rateSong(rating);
        for (Song s: songs){
          System.out.print(s);
        }
        return;
        }
      }
      throw new IllegalArgumentException("Song not found:" + title);
    }
    public void printAllSongs (){
      for (Song song: songs){
        System.out.print(song); 
      }
    }
  }
  

         
    
