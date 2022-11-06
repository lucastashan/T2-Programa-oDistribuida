import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static volatile int[] cont = {0, 1, 2};

	//Minhas configuracoes lidas do arquivo
	public static Configuracao myConfig;
	//Demais configuracoes lidas do arquivo
	public static List<Configuracao> otherConfigs;
	//Meu Vetor local para contasgem dos tempos
	public static Vetor vetor;

	public static void main(String[] args) throws IOException {
		otherConfigs = new ArrayList<Configuracao>();
		List<String> listConfigsString = LeArquivo("config.txt");
		Configure(args[0], listConfigsString);
		vetor = new Vetor(otherConfigs.size() + 1);

		//Configura o multicast
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(4321);
		InetAddress grupo = InetAddress.getByName("230.0.0.0");
		socket.joinGroup(grupo);

		//Fica enviando mensagens ate que todos os processos estejam prontos 
		SendArrived sendArrived = new SendArrived(myConfig.port);
		sendArrived.start();

		//Fica escutando as mensagens e conferindo os processos ate que todos estejam prontos para iniciar juntos
		System.out.println("Esperando pelos outros...");
		while (true) {
			//------------------------
			//Espera o recebimento da mensagem
			System.out.println(".");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			//Recebe a porta por mensagem de outros processos e converte para inteiro para comparacoes
			int receivedMessage = Integer.parseInt(new String(packet.getData(), packet.getOffset(), packet.getLength()));

			//Confere se nao recebeu a mensagem de si mesmo e entao marca o processo que enviou como pronto 
			if(receivedMessage != myConfig.port){
				System.out.println("Received message: " + receivedMessage);
				for(Configuracao c : otherConfigs)
					if(c.port == receivedMessage) c.ready = true;
			}
			//----------------------

			if(isReady())
				break;
		}
		//Envia uma ultima mensagem depois de pronto e para o multicast
		sendArrived.send();
		sendArrived.stopMulticast();
		System.out.println("Iniciando o envio de mensagens...");

		//Inicia a geracao de eventos, ao mesmo tempo a thred iniciada anteriormente passa a escutar as demais mensagens
		Random rng = new Random();
		int eventsCount = 0;
		while (eventsCount < myConfig.events) {
			float event = rng.nextFloat();
			int delay = rng.nextInt(myConfig.min_delay, myConfig.max_delay);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			//Evento local
			if (event > myConfig.chance) {
				System.out.println("Evento local...");
			}
			//Evento de envio de mensagem
			else {
				System.out.println("Evento de envio");
				int random = rng.nextInt(otherConfigs.size());
				DatagramSocket datagramSocket = new DatagramSocket(9010);
				byte[] relogio = "Ola".getBytes();
				System.out.println("RANDOM: "+random+" PORTSIZE: "+otherConfigs.size());
				DatagramPacket datagramPacket = new DatagramPacket(relogio, relogio.length, otherConfigs.get(random).nodeIp, otherConfigs.get(random).port);
				datagramSocket.send(datagramPacket);
				datagramSocket.close();
			}
			// System.out.println(evento);
			eventsCount++;
		}
	}

	//Retorna True se a aplicacao pode ser iniciada, ou seja, se todos os processos estiverem prontos
	private static boolean isReady(){
		for(Configuracao c : otherConfigs)
			if(c.ready == false)
				return false;

		return true;
	}

	//Le o arquivo e retorna uma lista com todas as configuracoes
	private static List<String> LeArquivo(String nomeArq) throws FileNotFoundException{
		List<String> listConfigs = new ArrayList<String>();
		File configFile = new File(nomeArq);
		Scanner reader = new Scanner(configFile);

		while(reader.hasNextLine()){
			listConfigs.add(reader.nextLine());
		}
		reader.close();

		System.out.println(listConfigs.toString());
		return listConfigs;
	}

	//Configura a aplicacao com a sua configuracao local, e carrega as demais configuracoes em uma lista para a troca de informacoes
	//Recebe o primeiro argumento da lista de comandos para setar a sua propria configuracao, seu id. Ex a primeira config da lista = 1
	private static void Configure(String id, List<String> listConfigsString){
		//Adiciona todas as configuracoes a lista
		for(String s : listConfigsString)
			otherConfigs.add(new Configuracao(s));

		//Remove a minha da lista e salva ela em myConfig
		for(int i=0; i<otherConfigs.size();i++)
			if (otherConfigs.get(i).id == Integer.parseInt(id)){
				myConfig = otherConfigs.remove(i);
			}
		
		System.out.println("MyConfig = " + myConfig.toString());
		System.out.println("OtherConfigs = " + otherConfigs.toString());
	}
}