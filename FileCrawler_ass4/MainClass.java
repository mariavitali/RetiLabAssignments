/**
 * @author Maria Vitali, 548154
 */

public class MainClass {
    public static void main(String[] args) {
        // MainClass directory k
        //      directory   (String) path of directory to examine
        //      k           (int) number of Consumers, k > 0

        if(args.length != 2){
            System.out.println("Use: MainClass directoryPath numberOfConsumers\n");
            System.exit(1);
        }
        String directory = args[0];
        int k = Integer.parseInt(args[1]);        // #consumers

        System.out.println("Input directory: " + directory);
        System.out.println("Number of consumers: " + k);
        if(k <= 0){
            System.out.println("\nNo consumers. Terminating...\n");
            System.exit(0);
        }

        SafeLinkedList queue = new SafeLinkedList();

        /*start producer*/
        Producer producer = new Producer(queue, directory);
        producer.start();

        /*start k consumers*/
        Consumer[] consumers = new Consumer[k];
        for(int i=0; i<k; i++){
           consumers[i] = new Consumer(queue);
           consumers[i].start();
        }

        try{
            producer.join();
        }catch(Exception e){
            e.getStackTrace();
        }
        System.out.println("\n\n**********Producer Terminated***********\n\n");

        for(int i=0; i<k; i++){
            consumers[i].setProducerDone();
        }
        for(int i=0; i<k; i++){
            try {
                consumers[i].join();
            }catch (Exception e){
                e.getStackTrace();
            }
        }

        System.out.println("\n\n*******END********\n\n");

    }
}
