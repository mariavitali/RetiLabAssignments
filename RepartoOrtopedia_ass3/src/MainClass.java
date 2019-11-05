import static java.lang.Integer.parseInt;

/**
 * Reparto Ortopedia comprendente un'equipe medica e un gestore dei pazienti.
 * I pazienti arrivano con diverso codice di urgenza e il gestore manager se ne occupa.
 * Ogni paziente deve fare k visite.
 * Termina quando tutti i pazienti hanno completato tutte le visite.
 *
 * @author Maria Vitali, 548154
 */


public class MainClass {
    /*MainClass requires pazientiRossi, pazientiGialli, pazientiBianchi
    * pazientiRossi  (int) numero di pazienti con codice Rosso
    * pazientiGialli  (int) numero di pazienti con codice Giallo
    * pazientiBianchi  (int) numero di pazienti con codice Bianco
    */
    public static void main(String[] args) {
        final int numeroMedici = 10;
        int pazientiRossi = Integer.valueOf(args[0]).intValue();
        int pazientiGialli = Integer.valueOf(args[1]).intValue();
        int pazientiBianchi = Integer.valueOf(args[2]).intValue();

        if(pazientiRossi==0 && pazientiGialli==0 && pazientiBianchi==0){
            System.out.println("Nessun paziente oggi!");
            System.exit(0);
        }

        /*inizializzo il reparto*/
        int k = (((int)(Math.random()*10)) % 5) + 1;          //numero di visite da 1 a 5
        Gestore manager= new Gestore();
        Medici medici = new Medici(numeroMedici);

        System.out.println("Quanti pazienti ci sono?");
        System.out.println("Numero pazienti codice ROSSO: " + pazientiRossi);
        System.out.println("Numero pazienti codice GIALLO: "+ pazientiGialli);
        System.out.println("Numero pazienti codice BIANCO: " + pazientiBianchi);
        System.out.println();

        System.out.println("\nnumero di visite per paziente: " + k + "\n");

        //CodiceRosso
        for(int i=0; i<pazientiRossi; i++){
            Paziente p = new Paziente("rosso", "Paziente" + (i+1), medici, k);
            manager.aggiungi(p);
        }

        //CodiceGiallo
        for(int i=0; i<pazientiGialli; i++){
            Paziente p = new Paziente("giallo", "Paziente" + (i+1+pazientiRossi), medici, k);
            manager.aggiungi(p);
        }

        //CodiceBianco
        for(int i=0; i<pazientiBianchi; i++){
            Paziente p = new Paziente("bianco", "Paziente" + (i+1+pazientiRossi+pazientiGialli), medici, k);
            manager.aggiungi(p);
        }

        manager.startPazienti();
        manager.attendiTerminazione();

        System.out.println("\n\nVISITE TERMINATE!\n\n");

    }
}
