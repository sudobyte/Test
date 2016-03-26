
package server1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server1 extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    public Server1(){
        super("Choni Instant Messenger");
        userText=new JTextField();
        userText.setEditable(false);    //the text component is not user editabale
        userText.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                sendMessage(e.getActionCommand());
                userText.setText("");
            
            }
        });
        add(userText,BorderLayout.NORTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300,150);
        setVisible(true);
    }

    //set up and run the server
    public void startRunning(){
        try{
            server=new ServerSocket(6789,100);   //only a 100 ppl are allowed to sit in my port 6789
            while(true){
                try{
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                    //connect and have conversation
                 
                }catch(EOFException eofexception){     //eofexception represents eofile and end of stream exception
                
                showMessage("\n Server ended the connection!");
                }finally{
                    closeCrap();
                }
            }
            
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }
    
    //wait for connection, then display connection information
    private void waitForConnection() throws IOException{
            showMessage("Waiting for some1 to connect......\n");
            connection =server.accept();    //makes a connection when its connected to some1
            showMessage("Now connected to"+connection.getInetAddress().getHostAddress());
            
            
            
    }
    //get stream to send and receive data
    private void setupStreams() throws IOException{
        output =new ObjectOutputStream(connection.getOutputStream());
        output.flush();     //to clear buffer
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now set up \n");
        
    }
    
    //during the chat conversation
    private void whileChatting() throws IOException{
        String message="you are now conected!";
        sendMessage(message);
        ableToType(true);
        do{
                try{
                    message=(String)input.readObject();
                    showMessage("\n"+message);
                }catch(ClassNotFoundException classNotFoundException){
                    showMessage("\n i dont know WTF that user send!");
                }
           //have a conversation 
        }while(!message.equals("CLIENT - END"));
    }
    
    //close streams and sockets after we are done chatting
    private void closeCrap(){
        showMessage("\n Clossing connections.... \n");
        ableToType(false);
        try{
           output.close();
           input.close();
           connection.close();
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }
    //send a message to lient
    private void sendMessage(String message){
        try{
        
            output.writeObject("SERVER -"+message);
            output.flush();
            showMessage("\nSERVER-"+message);
        }catch(IOException ioe){
            chatWindow.append("\n ERROR! DUDE I CAN'T SEND THAT MESSAGE");
           
        }
    }
    
    //updates chatWindow
    private void showMessage(final String text){
        SwingUtilities.invokeLater(     
                    new Runnable(){
                        public void run(){
                            chatWindow.append(text);
                        }
                    }
        );      //for updating parts of gui using threads
    }
    
    //let the user type studd into their box
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(     
                    new Runnable(){
                        public void run(){
                            userText.setEditable(tof);
                        }
                    }
        ); 
    }
   
}
