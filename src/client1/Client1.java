
package client1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Client1 extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    
    //constructor
    public Client1(String host){
        super("Choni Client");
        serverIP=host;
        userText =new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(e.getActionCommand());
                userText.setText("");
                
            }
        }
        );
        
        add(userText,BorderLayout.NORTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow),BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    
    }
    
    //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            
            whileChatting();
        }catch(EOFException eOFException){
            showMessage("\n Clien terminated the connection");
        }catch(IOException io){
            io.printStackTrace();
            
        }finally{
            closeCrap();
        }
    }

    //connect to server
    private void connectToServer() throws IOException{
            showMessage("attemting connection.....\n");
            connection=new Socket(InetAddress.getByName(serverIP),6789);
            showMessage("Connected to:"+connection.getInetAddress().getHostAddress());
            
    }
   
    
    //set up streams to send and receive messages
    
    private void setupStreams() throws IOException{
            output =new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            showMessage("\n dude your streams are now good to go\n");
            
        
    }
    //while chatting with sever
    private void whileChatting() throws IOException{
            ableToType(true);
            do{
                try{
                    message =(String)input.readObject();
                    showMessage("\n"+message);
                    
                }catch(ClassNotFoundException classNotFoundException){
                        showMessage("\n i dont know that object type");
                }
                
            }while(!message.equals("SERVER - END"));
    }
    
    //close the streams and sockets
    private void closeCrap(){
        showMessage("\n closing crap down...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
            
            
        }catch(IOException exception){
            exception.printStackTrace();
        }
        
    }
    
    //send messages to server
    private void sendMessage(String message){
            try{
         
                output.writeObject("CLIENT - "+message);
                output.flush();
                showMessage("\nCLIENT - "+message);
            }catch(IOException exception){
                chatWindow.append("\n something messed up sending message!");

            }
    }
    
    //change/update chatWindow
    private void showMessage(final String m){
            SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    chatWindow.append(m);
                }
            });
    }
    
    //gives permission to type into the user box
    private void ableToType(final boolean tof){
     
        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    userText.setEditable(tof);
                }
            });
    }
}
