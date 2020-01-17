import java.util.LinkedList;

/**
 * LinkedList thread safe
 */
public class SafeLinkedList {
    LinkedList<String> queue;

    public SafeLinkedList(){
        queue = new LinkedList<>();
    }

    /*return and remove the first element of the list (synchronized)*/
    public synchronized String getFromList(){
        return queue.poll();
    }

    /* insert string dir at the end of the queue */
    public synchronized void putInList(String dir){
        queue.add(dir);
    }

    /* returns size of the queue */
    public synchronized int size(){
        return queue.size();
    }

}
