
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class c2c {
    public static void main(String[] args) throws URISyntaxException, IOException {
        File f=new File("D:\\a.txt");
        if(f.exists()){
            f.delete();
            new FirstPage().setVisible(true);
        }
        else{
            f.createNewFile();
            File loc= new File(c2c.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            System.out.println(loc.getParent());
            /*Runtime.getRuntime().exec("java -classpath .;c2c.jar;"+loc.getParent()+"/mysql-connector-java-5.1.10.jar;"
                    + loc.getParent()+"ttorrent-core-1.6-SNAPSHOT.jar;"
                    + loc.getParent()+"ttorrent-core-1.6-SNAPSHOT-javadoc.jar;"
                    + loc.getParent()+"ojdbc6.jar;"
                    + loc.getParent()+"ttorrent-cli-1.6-SNAPSHOT.jar;"
                    + loc.getParent()+"ttorrent-cli-1.6-SNAPSHOT-javadoc.jar;"
                    +" c2c");*/
            
            Runtime.getRuntime().exec("java -classpath .;c2c.jar;"
                    +loc.getParent()+"/a.jar;"
                    +loc.getParent()+"/b.jar;"
                    +loc.getParent()+"/c.jar;"
                    +loc.getParent()+"/d.jar;"
                    +loc.getParent()+"/e.jar;"
                    +loc.getParent()+"/f.jar;"
                    +loc.getParent()+"/g.jar;"
                    +loc.getParent()+"/h.jar;"
                    +loc.getParent()+"/i.jar;"
                    +loc.getParent()+"/j.jar;"
                    +loc.getParent()+"/k.jar;"
                    +loc.getParent()+"/l.jar;"
                    +" c2c");
            
            /*Runtime.getRuntime().exec("java -classpath "
                    + ".;ttorrent-cli-1.6-SNAPSHOT-jar-with-dependencies.jar;"
                    + "I:/mysql-connector-java-5.1.10.jar"
                    + " c2c");*/
            
        }
    }
}
