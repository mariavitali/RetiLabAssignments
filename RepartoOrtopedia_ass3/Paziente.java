/**
 * thread paziente
 */

public class Paziente extends Thread {
    private String codice;
    private String nome;
    private Medici medici;
    private int numvisite;
    private int k;
    private int indexMedico;

    /**
     * Inizializza un nuovo paziente
     * @param codice codice d'urgenza del paziente
     * @param nome nome del paziente
     * @param medici equipe medica a disposizione del paziente
     * @param k numero di visite che deve fare il paziente
     */

    public Paziente(String codice, String nome, Medici medici, int k){
        this.codice = codice;
        this.nome = nome;
        this.medici = medici;
        this.k = k;
        numvisite = 0;
        indexMedico = -1;
    }

    //smista le varie esecuzioni dei pazienti in base al codice
    public void run(){
        switch(codice){
            case "rosso":
                pazienteRosso();
                break;
            case "giallo":
                pazienteGiallo();
                break;
            default:
                pazienteBianco();
                break;
        }
    }


    public void pazienteRosso(){
        while (numvisite < k) {
            medici.iniziaVisitaRosso();
            System.out.println("[Rosso] I medici visitano il paziente " + nome);
            visita();
            numvisite++;
            System.out.println("[Rosso] " + nome + " ha finito la " + numvisite + "° visita.");
            medici.terminaVisitaRosso();
            if(numvisite < k)
                aspettaProssimaVisita();
        }
    }

    public void pazienteGiallo(){
        System.out.println("[Giallo] " + nome + " in coda per medico" + (indexMedico+1));
        while(numvisite < k){
            medici.iniziaVisitaGiallo(indexMedico);
            System.out.println("[Giallo] Medico" + (indexMedico+1) + " visita " + nome);
            visita();
            numvisite++;
            System.out.println("[Giallo] " + nome + " ha finito la " + numvisite + "° visita.");
            medici.terminaVisita(indexMedico);
            if(numvisite < k)
                aspettaProssimaVisita();
        }
    }

    public void pazienteBianco(){
        while(numvisite < k){
            int index = medici.iniziaVisitaBianco();
            System.out.println("[Bianco] Medico" + (index+1) + " visita " + nome);
            visita();
            numvisite++;
            System.out.println("[Bianco] " + nome + " ha finito la " + numvisite + "° visita.");
            medici.terminaVisita(index);
            if(numvisite < k)
                aspettaProssimaVisita();
        }
    }

    /**
     * simula il tempo di visita di un paziente
     */
    public void visita(){
        /*la visita dura un numero random di secondi*/
        long time = (long) (Math.random() * 1000);
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * simula il tempo di attesa tra una visita e la successiva
     */
    public void aspettaProssimaVisita(){
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setIndexMedico(int number)
            throws IllegalArgumentException {
        if(number < 0 || number > 9) {
            throw new IllegalArgumentException();
        }
        this.indexMedico = number;
    }


    public String getCodice(){
        return codice;
    }


}
