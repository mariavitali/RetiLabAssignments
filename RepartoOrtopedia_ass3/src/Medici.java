/**
 * monitor per la gestione dei medici (risorsa condivisa tra i thread)
 */

public class Medici {
    private boolean[] mediciFree;       // (mediciFree[i] == true) solo se il medico i in quel momento non sta visitando nessun paziente
    private int numBusy;                //numero di medici occupati
    private int countRossi;             //numero di pazienti rossi

    /**
     * costruttore
     * @param number numero dei medici che fanno parte dell'equipe
     */
    public Medici(int number){
        mediciFree = new boolean[number];
        for(int i=0; i<number; i++){
            mediciFree[i] = true;
        }
        numBusy = 0;
        countRossi = 0;
    }

    /**
     * assegnazione di tutti i medici a un paziente rosso
     */
    public synchronized void iniziaVisitaRosso(){
        countRossi++;
        while(numBusy>0){
            try {
                wait();
            }catch (Exception e){
                e.getStackTrace();
            }
        }
        for(int i=0; i<mediciFree.length; i++){
            mediciFree[i] = false;
            numBusy++;
        }
    }

    /**
     * rilascio di tutti i medici che hanno terminato la visita al paziente rosso
     */
    public synchronized void terminaVisitaRosso(){
        for(int i =0; i<mediciFree.length; i++){
            mediciFree[i] = true;
            numBusy--;
        }
        countRossi--;
        notifyAll();
    }

    /**
     * assegnazione del medico specificato al paziente giallo
     * @param indexMedico medico richiesto dal paziente giallo
     */

    public synchronized void iniziaVisitaGiallo(int indexMedico){
        while(mediciFree[indexMedico] == false || countRossi > 0){
            try{
                wait();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        mediciFree[indexMedico] = false;
        numBusy++;
    }

    /**
     * assegnazione del primo medico libero al paziente Bianco
     * @return indice del medico assegnato
     */
    public synchronized int iniziaVisitaBianco(){
        while(numBusy == mediciFree.length || countRossi > 0){
            try{
                wait();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        int i = 0, indexMedico = -1;
        while(i < mediciFree.length){
            if(mediciFree[i]==true) {
                indexMedico = i;
                break;
            }
            i++;
        }
        mediciFree[indexMedico] = false;
        numBusy++;

        return indexMedico;
    }

    /**
     * terminazione visita di gialli e bianchi e rilascio del medico assegnato
     * @param indexMedico indice medico che ha effettuato la visita
     */
    public synchronized void terminaVisita(int indexMedico){
        mediciFree[indexMedico] = true;
        numBusy--;
        notifyAll();
    }

}
