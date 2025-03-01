/* LARGE ASSIGNMENT #1 - MusicStore
 * Will Snider and Zeenya
 */

package view;

import java.util.ArrayList;

import model.Album;

public class Main {
    public static void main(String[] args) {
        MusicStore tester = new MusicStore();
        ArrayList<Album> test = tester.store.getAllAlbums();
        System.out.println(test.isEmpty());
        tester.mainLoop();
    }
}