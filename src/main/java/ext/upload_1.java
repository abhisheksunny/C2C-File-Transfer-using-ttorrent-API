package ext;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.Client.ClientState;
import com.turn.ttorrent.client.SharedTorrent;
import com.turn.ttorrent.tracker.TrackedTorrent;

import java.io.*;
import java.net.URISyntaxException;


public class upload_1 {
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, URISyntaxException {
        File parent = new File("C:\\Users\\ABHISHEK\\Desktop");
        Tracker tracker = new Tracker(new InetSocketAddress(6969));
        tracker.start();
        System.out.println("ttorrent tracker is running...");
        try {
          System.out.println("create new .torrent metainfo file...");
          String sharedFile = "C:\\Users\\ABHISHEK\\Desktop\\file.MKV";
          Torrent torrent = Torrent.create(new File(sharedFile), tracker.getAnnounceUrl().toURI(), "createdByME");
          
          System.out.println("save .torrent to file...");
          torrent.save(new FileOutputStream("C:\\Users\\ABHISHEK\\Desktop\\seed.torrent"));

          System.out.println("announce new .torrent available...");
          //tracker.announce(torrent);
         
          
            File torrentFile = new File("C:\\Users\\ABHISHEK\\Desktop\\seed.torrent");
            // Create torrent instance
            TrackedTorrent torr1 = new TrackedTorrent(Torrent.load(torrentFile, true));
            // Announce torrent
            tracker.announce(torr1);
            // Start the tracker
            //tracker.start();
          
          

          System.out.println("kick off seeder to share...");
          Client seeder = new Client(InetAddress.getLocalHost(), new SharedTorrent(torr1, parent, true));
          seeder.share();
          try {
            if (!ClientState.ERROR.equals(seeder.getState())) {
              BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
              try {
                reader.readLine();
              } finally {
                reader.close();
              }
            }
          } finally {
            seeder.stop();
          }
        } finally {
          tracker.stop();
        }
    }
}
