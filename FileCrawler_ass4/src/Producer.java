import java.io.File;

/**
 * Producer thread.
 * Explores recursively a directory, searching for other directories.
 * If one is found, the producer puts the directory's pathname in a queue.
 */
public class Producer extends Thread {
    private SafeLinkedList queue;
    private String initialPathname;

    public Producer(SafeLinkedList queue, String initialPathname){
        this.queue = queue;
        this.initialPathname = initialPathname;
    }

    @Override
    public void run(){
        File initialDirectory = new File(initialPathname);
        searchDir(initialDirectory);
    }

    /*recursive function visits dir
    * if dir is not a directory, do nothing*/
    public void searchDir(File dir){
        if(dir.isDirectory()) {
            System.out.println("Producer found a new directory: " + dir.getPath());
            queue.putInList(dir.getPath());
            /*cerca in maniera ricorsiva all'interno della cartella se ci sono altre cartelle*/
            File[] files = dir.listFiles();
            for (File file : files) {
                searchDir(file);
            }
        }
        else return;
    }

}
