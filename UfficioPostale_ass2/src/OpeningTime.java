public class OpeningTime implements Runnable{
    private long time;

    public OpeningTime(long time){
        this.time = time;
    }

    public void run(){
        try{
            Thread.sleep(time*1000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
