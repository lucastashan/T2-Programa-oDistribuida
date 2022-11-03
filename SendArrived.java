import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendArrived extends Thread {

    private int port;
    private boolean exit;
    public SendArrived(int port) {
        this.port = port;
        this.exit = false;
    }

    public void send() throws IOException{
        byte[] saida = new byte[256];
		String mens = Integer.toString(port);
		saida = mens.getBytes();
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName("230.0.0.0");
        DatagramPacket pacote = new DatagramPacket(saida,saida.length,grupo,4321);
        socket.send(pacote);
        socket.close();
    }  
    
    public void stopThread() {
        exit = true;
    }

    @Override
    public void run() {
        while(!exit) {
            try {
                send();
                Thread.sleep(1000);
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e){

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}