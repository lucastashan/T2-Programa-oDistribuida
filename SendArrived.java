import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SendArrived extends Thread {

    private int port;
    private boolean exit;

    public SendArrived(int port) {
        this.port = port;
        this.exit = false;
    }

    public void send() throws IOException {
        byte[] saida = new byte[256];
        String mens = Integer.toString(port);
        saida = mens.getBytes();
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName("230.0.0.0");
        DatagramPacket pacote = new DatagramPacket(saida, saida.length, grupo, 4321);
        socket.send(pacote);
        socket.close();
    }

    public void stopMulticast() {
        exit = true;
    }

    public Vetor max(Vetor vet1, Vetor vet2, int id) {
        Vetor vetor = new Vetor(vet1.vet.length);
        for (int i = 0; i < vetor.vet.length; i++) {
            vetor.vet[i] = vet1.vet[i] >= vet2.vet[i] ? vet1.vet[i] : vet2.vet[i];
        }
        vetor.vet[id - 1]++;
        return vetor;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                send();
                Thread.sleep(1000);
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e) {

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        exit = false;
        while (!exit) {
            byte[] buffer = new byte[1024];
            DatagramPacket receiveDatagram = new DatagramPacket(buffer, buffer.length);
            try {
                // Em 5 segundo ele ve se exit igual true
                Main.datagramSocket.setSoTimeout(5000);
                Main.datagramSocket.receive(receiveDatagram);

                System.out.println("Recebeu mensagem...");
                InetAddress inetAddress = receiveDatagram.getAddress();
                int porta = receiveDatagram.getPort();
                // manda o OK
                byte[] sok = "OK".getBytes();
                DatagramPacket dpok = new DatagramPacket(sok, sok.length, inetAddress, porta+1);
                Main.datagramSocket.send(dpok);
                String message = new String(buffer);
                String[] campos = message.split(",");
                String s = campos[campos.length - 1].trim();
                String received_vet = campos[0];
                for (int i = 1; i < campos.length - 1; i++) {
                    received_vet += "," + campos[i];
                }
                Vetor vetor_recebido = new Vetor(received_vet);
                Main.vetor = max(vetor_recebido, Main.vetor, Main.myConfig.id);
                System.out.println(Main.myConfig.id + Main.vetor.toString() + " R " + s + vetor_recebido.toString());
                System.out.println();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}