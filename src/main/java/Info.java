
public class Info {
    //public String hostIP="192.168.0.104";
    public String hostIP="192.168.43.44";
    //public String hostIP="localhost";
    public String USER;
    public Info setValues(String user, String host){
        Info info=new Info();
        info.USER=user;
        info.hostIP=host;
        return info;
    }
}
