import java.net.InetAddress;

public class Configuracao {
    public int id;
	public InetAddress nodeIp;
	public int port;
	public float chance;
	public int events;
	public int min_delay;
	public int max_delay;
    //Variavel usada para verificar se todos os processos estao prontos para iniciar
    public boolean ready;

    //Recebe uma linha do arquivo e transforma em uma configuracao
    public Configuracao(String line){
        String[] fields = line.split(" ");

        try {
            this.id = Integer.parseInt(fields[0]);
            this.nodeIp = InetAddress.getByName(fields[1]);
            this.port = Integer.parseInt(fields[2]);
            this.chance = Float.parseFloat(fields[3]);
            this.events = Integer.parseInt(fields[4]);
            this.min_delay = Integer.parseInt(fields[5]);
            this.max_delay = Integer.parseInt(fields[6]); 
            this.ready = false;
        } catch (Exception e) {
            System.out.println("Erro ao iniciar uma configuracao do arquivo ID:" + fields[0]);
        }  
    }

    //Construtor padrao
    public Configuracao(int id, InetAddress nodeIp, int port, float chance, int events, int min_delay, int max_delay){
        this.id=id;
        this.nodeIp=nodeIp;
        this.port=port;
        this.chance=chance;
        this.events=events;
        this.min_delay=min_delay;
        this.max_delay=max_delay;
    }

    public void printConfiguracao(){
        System.out.println(id+" "+nodeIp+" "+port+" "+chance+" "+events+" "+min_delay+" "+max_delay);
    }

    @Override
    public String toString(){
        return id+" "+nodeIp+" "+port+" "+chance+" "+events+" "+min_delay+" "+max_delay;
    }
}