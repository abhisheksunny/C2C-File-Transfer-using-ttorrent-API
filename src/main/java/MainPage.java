
import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.sql.*;
import javax.swing.*;
import org.apache.log4j.BasicConfigurator;

public class MainPage extends Frame implements ActionListener{

    JButton sendbtn, sendfilebtn, browsebtn, logoutbtn,viewbtn[],downStartbtn[],/*Uploadbtn[],*/downbtn[],uploadbtn[];
    JTextField msg, filepathtxt,txtdown[],txtupload[];
    JLabel userlbl,namelbl[],templbl,msglbl,uploadlbl[],downlbl[];
    JPanel userListPanel,chatPanel,uploadPanel,downloadPanel,messsageArea;
    JProgressBar downjb[];        
    
    File fileToSend;
    int count_down=-1,count_upload=-1;
    String USER,activeUser=null;
    Thread thdown[],thupload[];
    
    DBase db;
    Info info;
    
    JPanel downjp, uploadjp;
    GridBagConstraints downgbc, uploadgbc;  // GBC for upload  and download
    
    MainPage(Info in){
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                System.exit(0);
            }
        });
        info=in;
        System.out.println("Main 47 in "+in.USER+"..."+in.hostIP);
        USER=in.USER;
        db=new DBase(info);
        
        setLayout(null);
        userListPanel=new JPanel();
        //userListPanel.setBackground(Color.red);
        userListPanel.setBounds(0, 0, 300, 700);
        //userListPanel.setPreferredSize(new Dimension(50, 50));;
        userListPanel.setLayout(null);
        
        chatPanel=new JPanel();
        chatPanel.setBounds(300, 0, 700, 700);
        chatPanel.setLayout(null);
        
        uploadPanel=new JPanel();
        uploadPanel.setBounds(1000, 30, 345, 335);
        uploadPanel.setLayout(null);
        
        downloadPanel=new JPanel();
        downloadPanel.setBounds(1000, 360, 345, 335);
        downloadPanel.setLayout(null);
        
        this.add(userListPanel);
        this.add(chatPanel);
        this.add(uploadPanel);
        this.add(downloadPanel);
        
//-------------------------------PANEL ADDITION IS OVER-------------------------
       
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createLineBorder(Color.red));
        jp.setLayout(new GridBagLayout());
        //jp.setLayout(null);
        
        GridBagConstraints gbculp = new GridBagConstraints();//GBC for userListPanel
        gbculp.anchor=GridBagConstraints.NORTH;
        gbculp.gridwidth = GridBagConstraints.REMAINDER;
        gbculp.fill = GridBagConstraints.HORIZONTAL;
        gbculp.insets=new Insets(5, 5, 5, 5);
        
        viewbtn=new JButton[50];
        namelbl=new JLabel[50];
        
        String uname;
        ResultSet rs;
        int dblen=0,i=1;
        try {
            rs = db.stmt.executeQuery("select uname from user;");
            
            while (rs.next()) 
            {
                uname=(rs.getString(1));
                if(!uname.equals(USER))
                {
                    namelbl[i]=new JLabel(i+".     "+uname);
                    viewbtn[i]=new JButton("View");

                    namelbl[i].setPreferredSize(new Dimension(250, 50));
                    namelbl[i].setLayout(null);
                    namelbl[i].setBorder(BorderFactory.createLineBorder(Color.lightGray, 5));
                    viewbtn[i].setBounds(150, 5, 95, 40);
                    viewbtn[i].setName(uname);
                    viewbtn[i].addActionListener(new ActionListener() {//action performed by any VIEW button
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            for(int i=0;i<50;i++){
                                if(ae.getSource().equals(viewbtn[i])){  
                                    activeUser=viewbtn[i].getName();   //user with whom the current user is 
                                    templbl.setText(viewbtn[i].getName()+"..."+USER);
                                    try {
                                        msgAreaCreation();      //call to create msgArea
                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }
                                    break;
                                }
                            }
                        }
                    });
                    namelbl[i].add(viewbtn[i]);
                    //btn.setBounds(50, 50+(i*60), 100, 50);
                    jp.add(namelbl[i], gbculp);
                }
                i++;
            }
            for(int j=i;j<=20;j++){  //for creating extra spaces in the user menu
                namelbl[j]=new JLabel(" ");
                jp.add(namelbl[j], gbculp);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        JScrollPane scrollPane = new JScrollPane(jp);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        scrollPane.setBounds(10, 40, 290, 650);
        userListPanel.add(scrollPane);
        
        //scrollPane.setLayout(null);
        
//--------------------------USER LIST PANEL ADDITION IS OVER--------------------  
        
        
        userlbl=new JLabel();
        userlbl.setFont(new Font("Serif", Font.BOLD, 28));
        userlbl.setText("''"+USER+"''");
        userlbl.setBounds(20,40, 300, 40);
        chatPanel.add(userlbl);
        
        templbl=new JLabel();
        templbl.setText("...");
        templbl.setBounds(150,40, 100, 40);
        //chatPanel.add(templbl);
        
        logoutbtn = new javax.swing.JButton("LOGOUT");
        logoutbtn.setBounds(550, 40, 140, 40);
        chatPanel.add(logoutbtn);
        
        messsageArea=new JPanel();
        messsageArea.setBounds(10,90,680, 500);
        messsageArea.setBorder(BorderFactory.createLineBorder(Color.black));
        messsageArea.setBackground(Color.WHITE);
        messsageArea.setLayout(null);
        chatPanel.add(messsageArea);
        
        filepathtxt=new JTextField("File....", 255);
        filepathtxt.setBounds(10, 600, 380, 40);
        chatPanel.add(filepathtxt);
        
        browsebtn = new JButton("Browse ");
        browsebtn.setBounds(400, 600, 140, 40);
        chatPanel.add(browsebtn);
        browsebtn.setEnabled(false);
        
        sendfilebtn = new JButton("Send File");
        sendfilebtn.setBounds(550, 600, 140, 40);
        sendfilebtn.setEnabled(false);
        chatPanel.add(sendfilebtn);
        
        msg=new JTextField("Enter Message.....",255);
        msg.setForeground(Color.GRAY);
        msg.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (msg.getText().equals("Enter Message.....")) {
                    msg.setText("");
                    msg.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (msg.getText().isEmpty()) {
                    msg.setForeground(Color.GRAY);
                    msg.setText("Enter Message.....");
                }
            }
        });
        msg.setBounds(10, 650, 540, 40);
        chatPanel.add(msg);
        
        sendbtn = new JButton("Send Message");
        sendbtn.setBounds(550, 650, 140, 40);
        chatPanel.add(sendbtn);
        sendbtn.setEnabled(false);
        
        downStartbtn=new JButton[50];
        //Uploadbtn=new JButton[50];
//-----------------------------CHAT AREA ADDITION IS OVER-----------------------       
        
        
        uploadPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        downloadPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        downjp = new JPanel();
        downjp.setBorder(BorderFactory.createLineBorder(Color.red));
        downjp.setLayout(new GridBagLayout());
        downgbc = new GridBagConstraints();
        downgbc.anchor=GridBagConstraints.NORTH;
        downgbc.gridwidth = GridBagConstraints.REMAINDER;
        downgbc.fill = GridBagConstraints.HORIZONTAL;
        downgbc.insets=new Insets(5, 5, 5, 5);
            downjp.add(new JLabel("DOWNLOADS:"), downgbc);
        txtdown=new JTextField[20];
        downlbl=new JLabel[20];
        downbtn=new JButton[20];
        downjb=new JProgressBar[20];
        for(int c=0;c<20;c++)
        {
            downlbl[c]=new JLabel();
            downlbl[c].setPreferredSize(new Dimension(320,60));
            downlbl[c].setLayout(null);
            downlbl[c].setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
            downbtn[c]=new JButton("Pause");
            downbtn[c].setBounds(241, 0, 76, 30);
            downbtn[c].setName(c+"");
            downbtn[c].addActionListener(this);
            txtdown[c]=new JTextField(30);
            txtdown[c].setBounds(0, 0, 240, 30);
            downjb[c]=new JProgressBar();
            downjb[c].setBounds(0, 30, 319, 30);
            downjb[c].setStringPainted(true);
            downlbl[c].add(txtdown[c]);
            downlbl[c].add(downbtn[c]);
            downlbl[c].add(downjb[c]);
            //downlbl[c].setEnabled(false);
            downjp.add(downlbl[c], downgbc);
            downlbl[c].setVisible(false);
        }
        JScrollPane downsp = new JScrollPane(downjp);
        downsp.setBorder(BorderFactory.createLineBorder(Color.pink));
        downsp.setBounds(0,0, 345, 335);
        downloadPanel.add(downsp);
        
        uploadjp = new JPanel();
        uploadjp.setBorder(BorderFactory.createLineBorder(Color.red));
        uploadjp.setLayout(new GridBagLayout());
        uploadgbc = new GridBagConstraints();
        uploadgbc.anchor=GridBagConstraints.NORTH;
        uploadgbc.gridwidth = GridBagConstraints.REMAINDER;
        uploadgbc.fill = GridBagConstraints.HORIZONTAL;
        uploadgbc.insets=new Insets(5, 5, 5, 5);
            uploadjp.add(new JLabel("UPLOADS: "), uploadgbc);
        txtupload=new JTextField[20];
        uploadlbl=new JLabel[20];
        uploadbtn=new JButton[20];
        for(int c=0;c<20;c++)
        {
            uploadlbl[c]=new JLabel();
            uploadlbl[c].setPreferredSize(new Dimension(320,60));
            uploadlbl[c].setLayout(null);
            uploadlbl[c].setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
            uploadbtn[c]=new JButton("Stop");
            uploadbtn[c].setBounds(200, 30, 100, 30);
            uploadbtn[c].setName(c+"");
            uploadbtn[c].addActionListener(this);
            txtupload[c]=new JTextField(30);
            txtupload[c].setBounds(2, 0, 315, 30);
            uploadlbl[c].add(txtupload[c]);
            //uploadlbl[c].add(uploadbtn[c]);
            //uploadlbl[c].setEnabled(false);
            uploadjp.add(uploadlbl[c], uploadgbc);
            uploadlbl[c].setVisible(false);
        }   
        JScrollPane uploadsp = new JScrollPane(uploadjp);
        uploadsp.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        uploadsp.setBounds(0,0, 345, 335);
        uploadPanel.add(uploadsp);
        
        thdown=new Thread[20];
        thupload=new Thread[20];
        
        
        sendbtn.addActionListener(this);
        sendfilebtn.addActionListener(this);
        browsebtn.addActionListener(this); 
        logoutbtn.addActionListener(this);
        
    }//   END of Constructor
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource().equals(sendbtn)){     // sending messeges to server
            String s=msg.getText(); 
            String sql = "INSERT INTO chat(sender_id,recv_id, msg, type) "
                    + "VALUES ('"+USER+"','"+activeUser+"','"+s+"','m')";
            try {
                db.stmt.executeUpdate(sql);
                msgAreaCreation();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            msg.setText(null);
        }
        else if(ae.getSource().equals(browsebtn)){      // BROWSE
            JFileChooser jfc = new JFileChooser();
            jfc.showOpenDialog(this);
            fileToSend = jfc.getSelectedFile();
            if(fileToSend!=null)
                filepathtxt.setText(fileToSend.getAbsolutePath());
            sendfilebtn.setEnabled(true);
        }
        
        else if(ae.getSource().equals(sendfilebtn)){
            filepathtxt.setText("File....");
            sendfilebtn.setEnabled(false);
            if(fileToSend!=null){
                uploadlbl[++count_upload].setVisible(true);
                txtupload[count_upload].setText(fileToSend.getName()+"  uploading...");
                uploadlbl[count_upload].setName(fileToSend.getName());
                
                thupload[count_upload] = new Thread(new Runnable() {
                        public void run() {
                            upload_main(fileToSend.getAbsolutePath());
                        }
                    });  
                thupload[count_upload].start();
            }
        }//if ae
        
        else if(ae.getSource().equals(logoutbtn)){
            USER=null;
            info=null;
            this.setVisible(false);
            new FirstPage().setVisible(true);
            
        }
            
        
        else{               // for PLAY and PAUSE Button
            for(int i=0;i<20;i++){
                if(ae.getSource().equals(downbtn[i])){
                    if(downbtn[i].getText().equals("Pause")){
                        downbtn[i].setText("Play");
                        thdown[i].suspend();
                    }
                    else if(downbtn[i].getText().equals("Play")){
                        downbtn[i].setText("Pause");
                        thdown[i].resume();
                    }
                }
                else if(ae.getSource().equals(uploadbtn[i])){
                    thupload[i].destroy();
                    uploadbtn[i].setEnabled(false);
                    txtupload[i].setText("Stopped");
                    /*
                    if(uploadbtn[i].getText().equals("Pause")){
                        uploadbtn[i].setText("Play");
                        thupload[i].suspend();
                    }
                    else if(uploadbtn[i].getText().equals("Play")){
                        uploadbtn[i].setText("Pause");
                        thupload[i].resume();
                    }
                    */
                }
            }
        }
        /*for(int i=0;i<50;i++){
            uploadlbl[++count_upload].setVisible(true);
            txtupload[count_upload].setText(uploadlbl[i].getName()+"  uploading...");
            upload_main(uploadlbl[i].getName());
            thupload[count_upload] = new Thread(new Runnable() {
                        public void run() {
                            upload_main(fileToSend.getAbsolutePath());
                        }
                    });  
            thupload[count_upload].start();
        }*/
            
        
        templbl.setText("I'm HERE!");
    }// end of ActionPerformed method
    
    
    void msgAreaCreation() throws SQLException{    // mixed with messageArea creation
        messsageArea.removeAll();
        browsebtn.setEnabled(true);
        sendbtn.setEnabled(true);
        JPanel jp = new JPanel();
        //jp.setBorder(BorderFactory.createLineBorder(Color.blue));
        jp.setBackground(Color.WHITE);
        jp.setLayout(new GridBagLayout());
        //jp.setLayout(null);

        GridBagConstraints gbcmsg = new GridBagConstraints();//GBC for MessageArea
        gbcmsg.anchor=GridBagConstraints.SOUTH;
        gbcmsg.gridwidth = GridBagConstraints.REMAINDER;
        gbcmsg.fill = GridBagConstraints.HORIZONTAL;
        gbcmsg.insets=new Insets(5, 5, 5, 5);

        JLabel waste=new JLabel();
        waste.setPreferredSize(new Dimension(650, 10));
        jp.add(waste,gbcmsg);

        ResultSet rs;
        String sql,type,ms,sid;
        java.sql.Timestamp ts;
        JLabel text,timetxt;
        int i=0;

        sql="select * from chat where "
            + "(sender_id='"+USER+"'AND recv_id='"+activeUser+"') "
            + "OR (recv_id='"+USER+"' AND sender_id='"+activeUser+"')";
        rs = db.stmt.executeQuery(sql);

        while(rs.next()){
            type=rs.getString("type");
            ts=rs.getTimestamp("time");
            sid=rs.getString("sender_id");
            ms=rs.getString("msg");
            
            msglbl=new JLabel();
            msglbl.setPreferredSize(new Dimension(650, 50));
            msglbl.setLayout(null);
            msglbl.setText("  "+sid+":");
            msglbl.setFont(new Font("Serif", Font.BOLD, 28));

            text=new JLabel();
            text.setFont(new Font("Serif", Font.PLAIN, 20));
            text.setBounds(100, 5, 550, 35);
            msglbl.add(text);

            timetxt=new JLabel();
            timetxt.setFont(new Font("Serif", Font.PLAIN, 10));
            timetxt.setBounds(550, 30, 100, 10);
            msglbl.add(timetxt);
            
            System.out.println(sid+"  "+ms);
            timetxt.setText(""+ts);
            
            if(type.equals("m"))
                text.setText(ms);
            else if(type.equals("f")){
                // TO DO
                if(ms.endsWith(".torrent"))
                    text.setText("<HTML><U>"+ms+"</U></HTML>");
                else continue;
                text.setForeground(Color.cyan);
                
                if(!sid.equals(USER)){
                    downStartbtn[i]=new JButton("Download");
                    downStartbtn[i].setName(ts+"");
                    downStartbtn[i].setBounds(300, 5, 150, 40);
                    downStartbtn[i].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            downbtnActionPerformed(e);
                        }
                    });
                    msglbl.add(downStartbtn[i]);
                    i++;
                }
               /* else{
                    Uploadbtn[i]=new JButton("Re-upload");
                    Uploadbtn[i].setName(ts+"");
                    Uploadbtn[i].setBounds(300, 5, 150, 40);
                    Uploadbtn[i].addActionListener(this);

                    i++;
                }*/
            }
            if(sid.equals(USER)){
                msglbl.setBorder(BorderFactory.createLineBorder(Color.blue));
                msglbl.setForeground(Color.blue);
            }
            else{
                msglbl.setBorder(BorderFactory.createLineBorder(Color.red));
                msglbl.setForeground(Color.red);
            }
            jp.add(msglbl, gbcmsg);
        }
        
        JScrollPane scrollPane = new JScrollPane(jp);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        scrollPane.setBounds(0, 0, 680, 500);
        messsageArea.add(scrollPane);
        messsageArea.updateUI();
    }
    
    public void downbtnActionPerformed(ActionEvent ae){
        for(int i=0;i<50;i++){
            if(ae.getSource().equals(downStartbtn[i])){
                File savepath;
                do{
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showOpenDialog(this);
                savepath = jfc.getSelectedFile();
                }while(savepath==null);
                
                downlbl[++count_down].setVisible(true);
                ResultSet rs=null;
                String  sql,time=downStartbtn[i].getName();
                System.out.println("time:"+time);
                try{
                    sql="select * from chat where time='"+time+"'";
                    PreparedStatement pstmt = db.con.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    rs.next();
                    
                    final File file = new File(savepath+"/"+rs.getString(3));
                    FileOutputStream output = new FileOutputStream(file);
                    //System.out.println("Writing to file " + file.getAbsolutePath());
                    System.out.println(rs.getString(1)+" . "+rs.getString(2)+" . "+rs.getString(3)
                        +" . "+rs.getString(5)+" . "+rs.getTimestamp(6)+" . ");
                    InputStream input = rs.getBinaryStream("file");
                    byte buffer[]=new byte[1]; 
                    while (input.read(buffer) > 0){
                        output.write(buffer);
                    }
                    // PUT JLABEL HERE
                    thdown[count_down] = new Thread(new Runnable() {
                        public void run() {
                            client_main(file.getAbsolutePath(),count_down);
                        }
                    });
                    thdown[count_down].start();
                    
                } catch (Exception ex) {
                    System.out.println("OTHER Exception: "+ex);
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception ex2) {
                        System.out.println(ex2.getMessage());
                    }
                }//finally
            }//if block
        }//for
    }//method
    
    void client_main(String fileDir,int i) {
        BasicConfigurator.configure();
        //File torrentPath = new File("C:\\Users\\ABHISHEK\\Desktop\\a.torrent");
        File torrentPath = new File(fileDir);
        File output = torrentPath.getParentFile();
        try {
            //SharedTorrent torrent = SharedTorrent.fromFile(torrentPath, output=new File("???"));
            SharedTorrent torrent = SharedTorrent.fromFile(torrentPath, output);
            System.out.println("Starting client for torrent: " + torrent.getName());
            Client client = new Client(InetAddress.getLocalHost(), torrent);
            System.out.println("/////////////////////////////////////////////////"+InetAddress.getLocalHost());
                    
            try {
                System.out.println("Start to download: " + torrent.getName());
                client.share(); 

                while(!Client.ClientState.SEEDING.equals(client.getState())) {
                    
                    if (Client.ClientState.ERROR.equals(client.getState())) {
                        throw new Exception("ttorrent client Error State");
                    }
                    System.out.printf("%f %% - %d bytes downloaded\n", torrent.getCompletion(), torrent.getDownloaded());
                    txtdown[i].setText(torrent.getName()+"  ("+ torrent.getDownloaded()+ " bytes)");
                    downjb[i].setValue((int)torrent.getCompletion());
                    //msg.setText(torrent.getCompletion()+"% - "+ torrent.getDownloaded()+ " bytes downloaded.");
                }

                System.out.println("//////////////////////////////////////////////////////////download completed.");
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
    
    void upload_main(String fileDir) {
        
        try {
            BasicConfigurator.configure();
            //String fileDir = "C:\\Users\\ABHISHEK\\Desktop\";
            //System.out.println("***************    "+InetAddress.getLocalHost().toString());
            Tracker t = new Tracker(InetAddress.getLocalHost());
            System.out.println("****************************"+InetAddress.getLocalHost());
            t.start();
            
            /****///g.print("Tracker started.");
            System.out.println("Analysing directory: " + fileDir);
            File uploadFile = new File(fileDir);
            File torrent_file = new File(uploadFile.getParentFile(), fileToSend.getName()+".torrent");
            Torrent torrent = Torrent.create(new File(uploadFile.getAbsolutePath()), new URI(t.getAnnounceUrl().toString()), "createdByME");
            /****///g.print("Created torrent " + torrent.getName() + " for file: " + uploadFile.getAbsolutePath());
            torrent.save(new FileOutputStream(torrent_file));
            
            torrentUpload(torrent_file);
            //System.out.println("*******************************************************"+torrent_file.length());
                    
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
    }// end upload_main
    
    void torrentUpload(File tfile) {
        //upload_main(tfile.getName());    ADD after THREAD
        try {                           
            //File file = new File("C:\\Users\\ABHISHEK\\Desktop\\a.java");
            FileInputStream input = new FileInputStream(tfile);

            String  sql="INSERT INTO chat (sender_id, recv_id, msg, file, type)"
                    + " VALUES('"+USER+"', '"+activeUser+"', '"+tfile.getName()+"', ?, 'f')";
            PreparedStatement pstmt = db.con.prepareStatement(sql);

            pstmt.setBinaryStream(1, input);
            System.out.println("('"+USER+"', '"+activeUser+"', '"+tfile.getName()+"', '"+input.toString()+"', 'f')");
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String[] args) {
        Info in=new Info();
        in.USER="a";
        in.hostIP="localhost";
        MainPage mp=new MainPage(in);
        mp.setName("c 2 c");
        mp.setSize(1350, 700);
        mp.setVisible(true);
        mp.setResizable(false);
    }

    
}
