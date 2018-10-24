package ext;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

import java.io.File;
import java.io.FilenameFilter;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class tracker_my {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // First, instantiate a Tracker object with the port you want it to listen on.
        // The default tracker port recommended by the BitTorrent protocol is 6969.
        Tracker tracker = new Tracker(new InetSocketAddress(6969));

        // Then, for each torrent you wish to announce on this tracker, simply created
        // a TrackedTorrent object and pass it to the tracker.announce() method:
        FilenameFilter filter = new FilenameFilter() {
          @Override
          public boolean accept(File dir, String name) {
            return name.endsWith("seed.torrent");
          }
        };

        
    for (File f : new File("C:\\Users\\ABHISHEK\\Desktop\\file.MKV").listFiles(filter)) {
      tracker.announce(TrackedTorrent.load(f));
    }
        
        //tracker.announce(TrackedTorrent.load(new File("C:\\Users\\ABHISHEK\\Desktop\\file.MKV")));
        // Once done, you just have to start the tracker's main operation loop:
        tracker.start();

        // You can stop the tracker when you're done with:
        tracker.stop();
    }
}
