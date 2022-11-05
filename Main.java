import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
	//Minha configuracao
	private static Configuracao configuracao;
	//Demais configuracoes
	private static List<Configuracao> configs;
	
	private static int qtdDeProc;
	private static List processos;
	private static List<Integer> ports;

	public static void main(String[] args) throws IOException {
		// Le arquivo de configuracao e usa a configuracao com o numero da linha passada por terminal no primeiro argumento
		LeituraArquivo(args[0]);

		// criando o multicast
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(4321);
		InetAddress grupo = InetAddress.getByName("230.0.0.0");
		socket.joinGroup(grupo);

		// vai ficar enviando ate todos chegarem
		SendArrived sendArrived = new SendArrived(configuracao.port);
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
		ports.stream()
				.forEach(System.out::println);

		// eventos

		// int ttl = socket.getTimeToLive();
		// socket.setTimeToLive(20);

		// byte[] bytePort = Integer.toString(port).getBytes();
		// DatagramPacket packet = new DatagramPacket(bytePort, bytePort.length, grupo,
		// 9000);
		// socket.send(packet);
		// socket.setTimeToLive(ttl);
	}

	public static void LeituraArquivo(String id) throws IOException {
		// configurando
		File confFile = new File("config.txt");
		Scanner reader = new Scanner(confFile);
		int linha = Integer.parseInt(id);
		LinkedList<InetAddress> hosts = new LinkedList<>();
		ports = new LinkedList<>();
		int i = 1;
		while (i < linha) {
			String[] campos = reader.nextLine().split(" ");
			hosts.add(InetAddress.getByName(campos[1]));
			ports.add(Integer.parseInt(campos[2]));
			i++;
		}
		configuracao = new Configuracao(reader.nextInt(),
				InetAddress.getByName(reader.next()), reader.nextInt(),
				reader.nextFloat(), reader.nextInt(), reader.nextInt(),
				reader.nextInt());
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
		qtdDeProc = i;
		processos = new LinkedList<Integer>();
	}
}