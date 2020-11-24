package producerconsumer;

public class Consumer extends Thread {
    Buffer buffer;
    boolean prendido;
    int time;
    int num;
    
    
    
    Consumer(Buffer buffer, int time, int num) {
        this.buffer = buffer;
        this.time = time;
        this.num = num;
        this.prendido = true;
    }
  
    @Override
    public void run() {
        System.out.println("Running Consumer...");
        String schemeOperation;
        
        while(this.prendido) {
            schemeOperation = this.buffer.consume(this.num);
            try {
                Thread.sleep(this.time);
            } catch (InterruptedException e) {
            }
        }
        
    }

}