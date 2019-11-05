/**
 * Increments the shared counters (thread safe).
 */


public class Counters {
    int countBonifico;
    int countAccredito;
    int countBollettino;
    int countF24;
    int countBancomat;

    public Counters(){
        countBonifico = 0;
        countAccredito = 0;
        countBollettino = 0;
        countF24 = 0;
        countBancomat = 0;
    }

    public synchronized void incrementBonifico(){
        countBonifico++;
    }
    public synchronized void incrementAccredito(){
        countAccredito++;
    }
    public synchronized void incrementBollettino(){
        countBollettino++;
    }
    public synchronized void incrementF24(){
        countF24++;
    }
    public synchronized void incrementBancomat(){
        countBancomat++;
    }

    /**
     * prints final results
     */
    public void printResults(){
        System.out.println("Bonifici totali = " + countBonifico);
        System.out.println("Accrediti totali = " + countAccredito);
        System.out.println("Bollettini totali = " + countBollettino);
        System.out.println("F24 totali = " + countF24);
        System.out.println("PagoBancomat totali = " + countBancomat);
    }

}
