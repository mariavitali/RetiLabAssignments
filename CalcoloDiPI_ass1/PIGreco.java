import java.util.Scanner;
import java.lang.Thread;
import java.util.Locale;

/**
 * @author Maria Vitali, matricola 548154
 * @version 11.0.4 2019-07-16
 */

public class PIGreco {
    public static void main(String[] args) {
        double accuracy;
        long maxTempoAttesa;

        Scanner keyboard = new Scanner(System.in);

        System.out.print("Specifica accuratezza del calcolo di pi-greco: ");
        accuracy = keyboard.useLocale(Locale.US).nextDouble();
        System.out.print("Specifica tempo massimo di attesa (secondi): ");
        maxTempoAttesa = keyboard.nextInt();

        maxTempoAttesa = maxTempoAttesa * 1000;

        CalcoloDiPI p = new CalcoloDiPI(accuracy);
        Thread t = new Thread(p);
        t.start();
        try{
            t.join(maxTempoAttesa);
            if(!t.isAlive())
                System.out.println("\nThread terminato prima dello scadere del tempo: accuracy raggiunta!\n");
        }
        catch(InterruptedException e){
            System.out.println(e);
            return;
        }

        if(t.isAlive()){
            System.out.println("\nTempo scaduto.");
            t.interrupt();
        }
    }
}
