import java.lang.Math;
import java.lang.Thread;

/**
 * Effettua il calcolo approssimato del PiGreco utilizzando la serie di Gregory-Leibniz.
 *
 * @author Maria Vitali, matricola 548154
 * @version 11.0.4 2019-07-16
 */

public class CalcoloDiPI implements Runnable {
    private double accuracy;
    private double estimatedPI;
    private boolean interr = false;

    /**
     * Inizializza il valore del PiGreco stimato e accuracy con il valore specificato.
     *
     * @param accuracy grado di accuratezza per il calcolo di PiGreco
     */

    public CalcoloDiPI(double accuracy) {
        this.accuracy = accuracy;
        estimatedPI = 0.0;
    }

    /**
     * Esegue il calcolo del PiGreco.
     *
     */

    public void run() {
        int i = 0;
        double div = 1;
        while(Math.abs(estimatedPI - Math.PI) >= accuracy && !interr) {
            if (Thread.interrupted()) {
                interr = true;
            } else {
                if (i % 2 == 0)
                    estimatedPI += (4 / div);
                else estimatedPI -= (4 / div);
                i++;
                div += 2;
            }
        }
        System.out.println("\nIl valore Math.PI è " + Math.PI);
        System.out.println("Il valore stimato di PI con la formula di Gregory-Leibniz è " + estimatedPI);
    }
}

