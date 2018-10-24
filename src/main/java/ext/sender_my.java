package ext;


import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;



/*
 * Copyright 2016 ABHISHEK.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 * @author ABHISHEK
 */
public class sender_my {

    /*public static void main(String[] args) throws Exception {
        File parent = new File("/Users/XXX/Movies/Fightings/");
        Tracker tracker = new Tracker(InetAddress.getLocalHost());
        tracker.start();
        System.out.println("ttorrent tracker is running...");
        try {
            System.out.println("create new .torrent metainfo file...");
            String sharedFile = "/Users/XXX/Movies/Fightings/RoK-FrontKick.asf";
            Torrent torrent = Torrent.create(new File(sharedFile), tracker.getAnnounceUrl().toURI(), "createdByDarren");

            System.out.println("save .torrent to file...");
            torrent.save(new File("seed.torrent"));

            System.out.println("announce new .torrent available...");
            tracker.announce(torrent);

            System.out.println("kick off seeder to share...");
            val seeder = new Client(InetAddress.getLocalHost, new SharedTorrent(torrent, parent, true));
            seeder.share();
            try {
                if (!ClientState.ERROR.equals(seeder.getState())) {
                    val reader = new BufferedReader(new InputStreamReader(System.in));
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
    }*/
}
