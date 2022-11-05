import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static volatile int[] cont = {0, 1, 2};

	public static void main(String[] args) throws IOException {
		// configurando
		File confFile = new File("config.txt");
		Scanner reader = new Scanner(confFile);
		int linha = Integer.parseInt(args[0]);
		LinkedList<InetAddress> hosts = new LinkedList<>();
		List<Integer> ports = new LinkedList<>();
		int i = 0;
		while (i < linha) {
			String[] campos = reader.nextLine().split(" ");
			hosts.add(InetAddress.getByName(campos[1]));
			ports.add(Integer.parseInt(campos[2]));
			i++;
		}
		int id = reader.nextInt();
		InetAddress nodeIp = InetAddress.getByName(reader.next());
		int port = reader.nextInt();
		float chance = reader.nextFloat();
		int events = reader.nextInt();
		int min_delay = reader.nextInt();
		int max_delay = reader.nextInt();
		if (reader.hasNextLine()) {
			reader.nextLine();
			i++;
		}

		// quantidade de processos no arquivo de configuracao
		while (reader.hasNextLine()) {
			String[] campos = reader.nextLine().split(" ");
			hosts.add(InetAddress.getByName(campos[1]));
			ports.add(Integer.parseInt(campos[2]));
			i++;
		}
		int qtdDeProc = i;
		List processos = new LinkedList<Integer>();

		// criando o multicast
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(4321);
		InetAddress grupo = InetAddress.getByName("230.0.0.0");
		socket.joinGroup(grupo);

		// vai ficar enviando ate todos chegarem
		SendArrived sendArrived = new SendArrived(port);
		sendArrived.start();

		// vai ficar escutando ate todos chegarem
		while (true) {
			if (processos.size() == qtdDeProc) {
				break;
			}
			System.out.println("Esperando pelos outros...");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String msg = new String(packet.getData(),
					packet.getOffset(), packet.getLength());
			if (!processos.contains(Integer.parseInt(msg))) {
				processos.add(Integer.parseInt(msg));
			}
			System.out.println(msg + " chegou.");
		}
		sendArrived.send();
		sendArrived.stopThread();
		System.out.println("Parou!");
		// ports.stream()
		// .forEach(System.out::println);

		// eventos

		// int ttl = socket.getTimeToLive();
		// socket.setTimeToLive(20);

		// byte[] bytePort = Integer.toString(port).getBytes();
		// DatagramPacket packet = new DatagramPacket(bytePort, bytePort.length, grupo,
		// 9000);
		// socket.send(packet);
		// socket.setTimeToLive(ttl);
		Random rand = new Random();
		int events_coun = 0;
		while (events_coun < events) {
			// for (int num : ports) {
			// 	System.out.println("LISTA: "+num);
			// }
			float evento = rand.nextFloat();
			int delay = rand.nextInt(min_delay, max_delay);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			// // Evento local
			if (evento > chance) {
			}
			// Evento de envio de mensagem
			else {
				int random = rand.nextInt(ports.size());
				DatagramSocket datagramSocket = new DatagramSocket(9010);
				byte[] relogio = "Ola".getBytes();
				System.out.println("RANDOM: "+random+" PORTSIZE: "+ports.size());
				DatagramPacket datagramPacket = new DatagramPacket(relogio, relogio.length, hosts.get(random), ports.get(random));
				datagramSocket.send(datagramPacket);
				datagramSocket.close();
			}
			// System.out.println(evento);
			events_coun++;
		}
	}
}