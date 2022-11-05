import java.net.InetAddress;

public class Configuracao {
    public int id;
	public InetAddress nodeIp;
	public int port;
	public float chance;
	public int events;
	public int min_delay;
	public int max_delay;

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
}
