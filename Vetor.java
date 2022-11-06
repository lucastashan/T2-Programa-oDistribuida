public class Vetor {
    public int[] vet;

    //Recebe o numero de outras configuracoes e inicia o array de int local
    public Vetor(int numConfigs){
        this.vet = new int[numConfigs];
    }

    //Recebe o array em string e transforma em um array de int, construtor usado para o recebimento de mensagens
    public Vetor(String stringVet){
        this.vet = stringToIntArray(stringVet);
    }

    // Formato do array de entrada = String "1,2,3,4,5"
    private int[] stringToIntArray(String stringVet){
        String[] aux = stringVet.split(",");
        int[] vet = new int[aux.length];
        for(int i=0;i<aux.length;i++){
            vet[i] = Integer.parseInt(aux[i]);
        }
        return vet;
    }
}
