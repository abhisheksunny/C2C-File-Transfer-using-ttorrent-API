
import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.Client.ClientState;
import com.turn.ttorrent.client.SharedTorrent;

public class client_f {

    public static final String DEFAULT_TRACKER_URI = "http://localhost:6969/announce";
    private int value=0;
    FSC tg;
    
    public client_f(String fileDir) {
            //tg=new FSC();
            //tg.setVisible(true);
            
            func(fileDir);
        }
    
    public void func(String fileDir){
        
                BasicConfigurator.configure();

        //File torrentPath = new File("C:\\Users\\ABHISHEK\\Desktop\\a.torrent");
        File torrentPath = new File(fileDir);
        File output = torrentPath.getParentFile();
        try {
            //SharedTorrent torrent = SharedTorrent.fromFile(torrentPath, output=new File("???"));
            final SharedTorrent torrent = SharedTorrent.fromFile(torrentPath, output);
            System.out.println("Starting client for torrent: " + torrent.getName());
            Client client = new Client(InetAddress.getLocalHost(), torrent);
            
            /*
            Thread th=new Thread(new Runnable() {
                @Override
                public void run() {
                    FSC.progress((int)torrent.getCompletion());
                }
            });
            th.start();
            */
                    
            try {
                System.out.println("Start to download: " + torrent.getName());
                client.share(); 

                while (!ClientState.SEEDING.equals(client.getState())) {
                    if (ClientState.ERROR.equals(client.getState())) {
                        throw new Exception("ttorrent client Error State");
                    }
                   // System.out.printf("%f %% - %d bytes downloaded\n", torrent.getCompletion(), torrent.getDownloaded());
                       //System.out.printf("\n\n%f \n\n",torrent.getCompletion());
                    //new FSC().jTextField2.setText(""+(int)torrent.getCompletion());
                }

                System.out.println("download completed.");
            } catch (Exception e) {
                System.err.println("An error occurs...");
                e.printStackTrace(System.err);
            } finally {
                System.out.println("stop client.");
                client.stop();
            }
        } catch (Exception e) {
            System.err.println("An error occurs...");
            e.printStackTrace(System.err);
        }
    }
}

