import java.util.LinkedList;

/**
 * Gestore del reparto.
 *
 * @author Maria Vitali, 548154
 */


public class Gestore {
    //private Integer[] medici;
    private LinkedList<Paziente> pazientiRossi;
    private LinkedList<Paziente> pazientiGialli;
    private LinkedList<Paziente> pazientiBianchi;

    /**
     * metodo costruttore, crea una lista per ogni codice di urgenza
     */
    public Gestore(){
        //this.medici = medici;
        pazientiRossi = new LinkedList<>();
        pazientiGialli = new LinkedList<>();
        pazientiBianchi = new LinkedList<>();
    }

    /**
     * aggiunge un paziente alla lista giusta in base al suo codice di urgenza e setta la priorità dei thread Paziente
     * @param p paziente da aggiungere
     */
    public void aggiungi(Paziente p){
        switch(p.getCodice()){
            case "rosso":
                p.setPriority(Thread.MAX_PRIORITY);
                pazientiRossi.add(p);
                break;
            case"giallo":
                p.setPriority(Thread.NORM_PRIORITY);
                int indexMedico = (int)(Math.random()*10);
                p.setIndexMedico(indexMedico);
                pazientiGialli.add(p);
                break;
            default :
                p.setPriority(Thread.MIN_PRIORITY);
                pazientiBianchi.add(p);
                break;
        }
    }

    /**
     * avvia i thread Paziente
     */
    public void startPazienti(){
        for (Paziente p:
                pazientiRossi) {
            p.start();
        }
        for (Paziente p:
                pazientiGialli) {
            p.start();
        }
        for (Paziente p:
                pazientiBianchi) {
            p.start();
        }
    }

    /**
     * attende la terminazione di tutti i thread delle 3 liste
     */

    public void attendiTerminazione(){
        /*attendo il termine delle visite dei pazienti rossi*/
        for (Paziente p:
                pazientiRossi) {
            try {
                p.join();
            }catch (Exception e){
                e.getStackTrace();
            }

        }
        /*attendo il termine delle visite dei pazienti gialli*/
        for (Paziente p:
                pazientiGialli) {
            try {
                p.join();
            }catch (Exception e){
                e.getStackTrace();
            }
        }
        /*attendo il termine delle visite dei pazienti bianchi*/
        for (Paziente p:
                pazientiBianchi) {
            try {
                p.join();
            }catch (Exception e){
                e.getStackTrace();
            }
        }
    }
}
