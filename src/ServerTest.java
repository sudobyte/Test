
import javax.swing.JFrame;
import server1.Server1;

public class ServerTest {
    public static void main(String[] args) {
        Server1 choni=new Server1();
        choni.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        choni.startRunning();
    }
    
}
