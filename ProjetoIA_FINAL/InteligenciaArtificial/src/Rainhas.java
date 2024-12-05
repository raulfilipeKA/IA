import java.util.Arrays;

public class Rainhas {

    private int[] tabuleiro;

    Rainhas() {
        tabuleiro = new int[8];
        int[] aux = new int[8];

        for(int i = 0; i < tabuleiro.length; i++) {
            aux[i] = -1;
        }
        double sample = -2;
        for(int i = 0; i < tabuleiro.length; i++) {
           do { 
                 sample = (Math.random() * 8);
                 tabuleiro[i] = (int) sample;
                
            } while(aux[tabuleiro[i]] != -1);
           aux[tabuleiro[i]] = tabuleiro[i];
        }
        System.out.println(Arrays.toString(tabuleiro));
    }
    public static void main(String[] args) {
        // Create an instance of your class
        Rainhas rainhas = new Rainhas();
        // The constructor will be executed, and the array will be printed
    }

}
