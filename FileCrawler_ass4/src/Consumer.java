import java.io.File;

/**
 * Consumer thread.
 * Gets from a queue a directory's pathname and prints its content.
 */
public class Consumer extends Thread {
    private SafeLinkedList queue;
    private boolean producerDone;       // true if the producer thread terminated

    public Consumer(SafeLinkedList list){
        this.queue = list;
        this.producerDone = false;
    }

    @Override
    public void run(){
        while(!producerDone || queue.size() > 0){
            try {
                String file = queue.getFromList();
                File dir = new File(file);
                System.out.println("Consumer " + Thread.currentThread().getName() + " retrieved directory: " + dir.getName());
                String[] files = dir.list();
                for (String f: files) {
                    System.out.println("--- [" + Thread.currentThread().getName() + "] " + f + " in directory " + dir.getName());
                }
            }catch (Exception e){
                e.getStackTrace();
            }
        }
    }

    /*the producer thread has terminated*/
    public void setProducerDone(){
        this.producerDone = true;
    }



}
