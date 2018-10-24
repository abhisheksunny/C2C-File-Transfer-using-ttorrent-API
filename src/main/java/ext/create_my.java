package ext;

import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;


public class create_my {

    public static void main(String[] args) {

        // File parent = new File("d:/echo-insurance.backup");
        String sharedFile = "C:\\Users\\ABHISHEK\\Desktop\\file.MKV";

        try {
            Tracker tracker = new Tracker( InetAddress.getLocalHost() );
            tracker.start();
            System.out.println("Tracker running.");

            System.out.println( "create new .torrent metainfo file..." );
            Torrent torrent = Torrent.create(new File(sharedFile), tracker.getAnnounceUrl().toURI(), "createdByME");

            System.out.println("save .torrent to file...");

            FileOutputStream fos = new FileOutputStream("C:\\Users\\ABHISHEK\\Desktop\\seed.torrent");
            torrent.save( fos );            
            fos.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
