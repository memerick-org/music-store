/* LARGE ASSIGNMENT #2 - MusicStore
 * Will Snider and Zeenya Ramirez
 */

package view;

import java.util.Collection;

import model.Album;

public class Main {
    public static void main(String[] args) {
        MusicStore tester = new MusicStore();
        Collection<Album> test = tester.store.getAllAlbums();
        System.out.println(test.isEmpty());
        tester.mainLoop();
    }
}