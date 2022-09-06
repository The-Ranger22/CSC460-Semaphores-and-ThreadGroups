import java.util.concurrent.Semaphore;

/**
 * @author Levi Schanding
 * @version 1.0
 */
public class Customer extends Thread {
    private volatile static Semaphore countMutex = new Semaphore(1);

    private volatile Semaphore nap;
    private volatile Semaphore door;
    private volatile Semaphore servicing;
    private static int initCount = 0; //used when assigning thread names
    private static int cusCount = 0; //used to
    private int id; //Likely frivolous, used to remember when the thread was admitted

    Customer(ThreadGroup threadGroup, Semaphore nap, Semaphore door, Semaphore servicing){

        super(threadGroup, "C" + ++initCount);
        this.nap = nap;
        this.door = door;
        this.servicing = servicing;

    }


    @Override
    public void run(){
        nap.release();

        try{
            System.out.print(ACT.ENTERING.msg);
            door.acquire();
        } catch (InterruptedException e){ }
        try{
            countMutex.acquire();
            this.id = ++cusCount;

        }catch(InterruptedException e){
        }finally {
            countMutex.release();
        }

        try{
            System.out.printf(ACT.ENTERED.msg, this.id);
            System.out.printf(ACT.WAITING.msg, this.id);
            servicing.acquire();
            System.out.printf(ACT.SERVED.msg, this.id);
        }catch (InterruptedException e){}
        System.out.printf(ACT.LEAVING.msg, this.id);

        door.release();
    }


    /**
     * Customer.ACT contains all output message formats for customer. Likely overkill, but helps keep things tidy.
     */
    private enum ACT{
        ENTERING("Customer attempting to enter restaurant.\n"),
        ENTERED("Customer %d has entered restaurant and is seated.\n"),
        WAITING("Customer %d is waiting for the waiter.\n"),
        SERVED("Customer %d has been served.\n"),
        LEAVING("Customer %d is leaving.\n");

        private final String msg;
        ACT(String msg){
            this.msg = msg;
        }


    }

}
