import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Maria Vitali, 548154
 *
 * creates a json database for clients bank accounts and counts the number of different transactions the clients made in the past 2 years.
 *
 */

public class MainClass {
    /* MainClass requires numClients, numTransactions
    * numClients = (int) number of clients with a bank account at this bank
    * maxTransactions = (int) max number of transactions per client in the past 2 years
    *
    *
    * if not specified as main arguments, default values will be used.
    * numClientsDEFAULT = 20
    * maxTransactionsDEFAULT = 50
    * */

    static final int numClientsDEFAULT = 40;
    static final int numTransactionsDEFAULT = 60;

    public static void main(String[] args) {
        int numClients, numTransactions;

        if(args.length != 2){
            numClients = numClientsDEFAULT;
            numTransactions = numTransactionsDEFAULT;
        }
        else{
            numClients = Integer.parseInt(args[0]);
            numTransactions = Integer.parseInt(args[1]);
        }

        //create bank database
        String path = "bankDB.json";
        BankAccountsCreator bankAccounts = new BankAccountsCreator(numClients, numTransactions);
        bankAccounts.createBankAccounts(path);
        System.out.println("\nBank database created!\n");



        //Counters object will deal with shared values
        Counters count = new Counters();

        //thread pool to perform counting operations
        int numHandlers = 5;        //number of handler threads of the pool
        ThreadPoolExecutor handlers = new ThreadPoolExecutor(numHandlers, numHandlers, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        //read and parse file .json
        File bankDB = new File(path);
        Reader reader = new Reader(handlers, count, bankDB);
        reader.start();

        try{
            /*wait for the reader to terminate*/
            reader.join();
            /*thread pool shutdown
            thread pool is closed, no new task can be queued (already queued tasks will be executed)*/
            handlers.shutdown();
            /*waits for threads termination*/
            handlers.awaitTermination(120L, TimeUnit.SECONDS);
            System.out.println("\nDone!\n\n");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("\nNumber of clients: " + numClients);
        System.out.println("Number of transactions per client: " + numTransactions);
        System.out.println();

        count.printResults();
    }
}
