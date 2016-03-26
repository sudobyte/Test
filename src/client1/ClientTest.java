
package client1;

import javax.swing.JFrame;

public class ClientTest {
    
    public static void main(String[] args) {
        Client1 neha;
        neha=new Client1("127.0.0.1");  //localhost
        neha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        neha.startRunning();
        
        
    }
    
}
