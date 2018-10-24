import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;

import org.apache.log4j.BasicConfigurator;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.FileOutputStream;

public class upload_final {

    public static final int DEFAULT_TRACKER_PORT = 6969;


    upload_final(String fileDir) 
    {
        /****///TempGUI g=new TempGUI();
        try {
            BasicConfigurator.configure();
            //String fileDir = "C:\\Users\\ABHISHEK\\Desktop\";
            //System.out.println("***************    "+InetAddress.getLocalHost().toString());
            Tracker t = new Tracker(InetAddress.getLocalHost());
            t.start();
            
            /****///g.print("Tracker started.");
            System.out.println("Analysing directory: " + fileDir);
            File uploadFile = new File(fileDir);
            File torrent_file = new File(uploadFile.getParentFile(), "a.torrent");
            Torrent torrent = Torrent.create(new File(uploadFile.getAbsolutePath()), new URI(t.getAnnounceUrl().toString()), "createdByME");
            /****///g.print("Created torrent " + torrent.getName() + " for file: " + uploadFile.getAbsolutePath());
            torrent.save(new FileOutputStream(torrent_file));
            
            TrackedTorrent tt = new TrackedTorrent(torrent);
            t.announce(tt);
            System.out.println("Torrent " + torrent.getName() + " announced");
            System.out.println("Sharing " + torrent.getName() + "...");
            /****///g.print("Uploading File.... ");
            Client seeder = new Client(InetAddress.getLocalHost(), new SharedTorrent(torrent, uploadFile.getParentFile(), true));
            seeder.share();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                reader.readLine();
            } finally {
                reader.close();
            }
            if (t != null) {
                t.stop();
                System.out.println("Tracker stopped.");
            }
        } 
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
