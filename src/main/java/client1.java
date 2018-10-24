import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class client1 {
    
    public client1(String fileDir) {
        
        try {
            //File downloadLocation = new File("C:\\Users\\ABHISHEK\\Desktop");
            //File torrLocation = new File("C:\\Users\\ABHISHEK\\Desktop\\a.torrent");
            
            File torrLocation = new File(fileDir);
            File downloadLocation =torrLocation.getParentFile();
            
            SharedTorrent sharedTorrent = SharedTorrent.fromFile(torrLocation, downloadLocation);
            
            Client client = new Client( InetAddress.getLocalHost(),sharedTorrent);
            
            client.setMaxDownloadRate(3000.0);
            client.setMaxUploadRate(3000.0);
            
           /* Thread th = new Thread(new Runnable() {
        float i=0;
            @Override
            public void run() {
                i=c.client.getTorrent().getCompletion();
                jProgressBar1.setValue((int)i);
            }
        }
        );*/
            
           // TempGUI.print("Download Started...");
            
            client.download();
            
            client.waitForCompletion();
            
            //TempGUI.print("Download Completed..."); 
       } 
        catch (Exception ex) {
            System.out.println(ex);
        
        }
    }

}
