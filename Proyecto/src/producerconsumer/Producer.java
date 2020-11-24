
package producerconsumer;

import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {
    Buffer buffer;
    int x;
    int y;
    int time;
    int num;
    
    boolean prendido;
    
    Producer(Buffer buffer, int timeout, int n, int m, int num) {
        this.buffer = buffer;
        this.time = timeout;
        this.x = n;
        this.y = m;
        this.num = num;
        this.prendido = true;
    }
    
    private String Operation(){
   
        ThreadLocalRandom r = ThreadLocalRandom.current();
        char ops = "+-*/".charAt(r.nextInt(4));
        String nums = "0123456789";
        
        char n1 = nums.charAt(r.nextInt(this.x, this.y));
        char n2 = nums.charAt(r.nextInt(this.x, this.y));
        String res = "("+ops + " " + n1 + " " + n2 + ")";
        
        return res;
    }
    
    @Override
    public void run() {
        System.out.println("Running Producer...");
        String schemeOp;
        
        while(this.prendido) {
            schemeOp = this.Operation();
            
            this.buffer.produce(schemeOp, this.num);
            
            try {
                Thread.sleep(this.time);
            } catch (InterruptedException ex) {
            }
        }
    }
    
}
