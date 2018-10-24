
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBase {
    /*
    int dblen = 0;
    String[] uid = new String[10];
    String[] uname = new String[10];
    String[] pass= new String[10];
    //*/
    Connection con = null;
    Statement stmt = null;
    
    //String hostIP = "192.168.43.44";
    String hostIP;
    
    ResultSet rs = null;
    
    DBase(Info info) 
    {
        hostIP=info.hostIP;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //con = DriverManager.getConnection("", "system", "admin");
            con = java.sql.DriverManager.getConnection("jdbc:mysql://"+hostIP+":3306/c2c", "root", "");
            stmt = con.createStatement();
            
            /**********************************************************************************
            Database 'c2c' Schema:
                * user(uid, uname, upass)
                *  * create table user(uid integer(5) auto_increment,uname varchar(10)not null ,upass varchar(10) not null, primary key(uid,uname));
                * user_details(uname, name, phone, email)
                * * create table user_details(uname varchar(10), name varchar(20) not null, phone integer(10), email varchar(50) not null, CONSTRAINT fk_username FOREIGN KEY (uname) REFERENCES user(uname));
            
            
            **********************************************************************************/
            
            
            /*
            rs = stmt.executeQuery("select * from user;");
            
            while (rs.next()) 
            {
                uid[dblen]=(rs.getString(1));
                uname[dblen]=(rs.getString(2));
                pass[dblen]=(rs.getString(3));
                System.out.println("**********   "+uid[dblen]+"    "+uname[dblen]+"    "+pass[dblen]);
                dblen++;
            }
            
            
            File file = new File("C:\\Users\\ABHISHEK\\Desktop\\a.jpg");
            FileInputStream input = new FileInputStream(file);
            
            String  sql="INSERT INTO chat (sender_id, recv_id, msg, file, type)"
                    + " VALUES('c', 'a', '"+file.getName()+"', ?, 'f')";
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            pstmt.setBinaryStream(1, input);
            System.out.println("'c', 'a', '"+file.getName()+"', '"+input.toString()+"', 'f'");
            pstmt.executeUpdate();
           
            
            File file = new File("C:\\Users\\ABHISHEK\\Desktop\\aaa.jpg");
            FileOutputStream output = new FileOutputStream(file);
            
            String  sql="select * from chat where time='2017-05-22 17:28:46'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            
            //System.out.println("Writing to file " + file.getAbsolutePath());
            while (rs.next()) {
                System.out.println(rs.getString(1)+" . "+rs.getString(2)+" . "+rs.getString(3)
                    +" . "+rs.getString(5)+" . "+rs.getTimestamp(6)+" . ");
                InputStream input = rs.getBinaryStream("file");
                byte buffer[]=new byte[1]; 
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
            }
            //*/
             
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: "+ex);
        } catch (SQLException ex) {
            System.out.println("SQLException: "+ex);
        } catch (Exception ex) {
            System.out.println("OTHER Exception: "+ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
            
        
    }
    public static void main(String[] args) {
        Info in=new Info();
        in.hostIP="localhost";
        new DBase(in);
    }
}
