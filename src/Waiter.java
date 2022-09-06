import java.util.concurrent.Semaphore;

/**
 * @author Levi Schanding
 * @version 1.0
 */
public class Waiter extends Thread{
    private volatile Semaphore nap;
    private volatile Semaphore servicing;

    Waiter(Semaphore nap, Semaphore servicing){
        this.nap = nap;
        this.servicing = servicing;
    }


    @Override
    public void run(){
        int cusCount = 0;
        do {
            if(!nap.tryAcquire()){
                try {
                    System.out.print(ACT.SLEEP.msg);
                    nap.acquire();
                    System.out.print(ACT.AWAKE.msg);

                }catch(InterruptedException e){
                    System.out.print(ACT.PASSED_OUT.msg);
                    return;
                }

            }else{

            }
            try{

                sleep(50);
            } catch (InterruptedException e){

            } finally {
                System.out.printf(ACT.SERVICING.msg, ++cusCount);
                servicing.release();
            }

        }while(true);
    }

    /**
     * Waiter.ACT contains all output message formats for waiter.
     */
    private enum ACT{
        SLEEP("Waiter is sleeping.\n"),
        AWAKE("Waiter is now awake.\n"),
        SERVICING("Waiter is now servicing customer %d.\n"),
        PASSED_OUT("The waiter has passed out.")
        ;
        private final String msg;
        ACT(String msg){
            this.msg = msg;
        }

    }


}
