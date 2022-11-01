import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        File confFile = new File("config.txt");
        Scanner reader = new Scanner(confFile);
        int linha = Integer.parseInt(args[0]);
        System.out.println(linha);
    }
}