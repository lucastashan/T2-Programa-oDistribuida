public class Vetor {
    public int[] vet;

    //Recebe o numero de outras configuracoes e inicia o array de int local
    public Vetor(int numConfigs){
        vet = new int[numConfigs];
    }

    //Recebe o array em string e transforma em um array de int, construtor usado para o recebimento de mensagens
    public Vetor(String stringVet){
        vet = stringToIntArray(stringVet);
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

    //Transforma o vetor em uma string formatada para ser enviado como mensagem
    public String formatToMessage(){
        String formatedMessage = "";
        for(int i=0;i<vet.length;i++){
            if(i==vet.length-1){
                formatedMessage+=vet[i];
            }else{
                formatedMessage+=vet[i]+",";
            }
        }
        return formatedMessage;
    }

    // metodo para o print
    public String toString() {
        String s = "[";
        for(int i = 0; i < vet.length - 1; i++) {
            s += Integer.toString(vet[i]) + ",";
        }
        s += Integer.toString(vet[vet.length-1]) + "]";
        return s;
    }
}
