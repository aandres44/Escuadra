
package producerconsumer;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class Buffer {
    
    public ArrayList<String> buffer;
    public DefaultTableModel tableH;
    public DefaultTableModel tableR;
    public JProgressBar progB;
    public JLabel labelR;
    public int size;
    public int done;
    
    Buffer(
            int size,
            JProgressBar progB,
            DefaultTableModel tableH,
            DefaultTableModel tableR,
            JLabel label)
    {
        this.size = size;
        this.progB = progB;
        this.tableH = tableH;
        this.tableR = tableR;
        this.labelR = label;
        this.buffer = new ArrayList<>();
        this.progB.setMinimum(0);
        this.progB.setMaximum(size);
        this.done = 0;
    }
    
    private String parseSch(char ch, int x, int y) {
        String str = "";
            switch (ch) {
                case '+' : str = Integer.toString(x+y); break;
                case '-' : str = Integer.toString(x-y); break;
                case '*' : str = Integer.toString(x*y); break;
                case '/' : 
                    if (y != 0) {
                        if (x % y == 0) str = Integer.toString(x/y);
                        else {
                            for (int i = 1; i < 10; i++){
                                if (x % i == 0 && y % i == 0){
                                    str = Integer.toString(x/i) + '/' + Integer.toString(y/i);
                                }
                            }
                        }
                    }
                    else str = "ERRoR";
                    break;
            }
        return str;
    }

    
    synchronized String consume(int n) {
        String product = "";
        
        while(this.buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                
            }
        }
        this.done++;
        this.labelR.setText("Tareas hechas : "+this.done+"");
        product = this.buffer.remove(this.buffer.size() - 1);
        String str = parseSch(product.charAt(1), Character.getNumericValue(product.charAt(3)), Character.getNumericValue(product.charAt(5)));
        String[] row = {product.charAt(1)+"",product.charAt(3)+"",product.charAt(5)+"",str, n+""};
        this.tableR.addRow(row);
        this.tableH.removeRow(this.buffer.size());
        this.progB.setValue(this.buffer.size());
 
        notify();
        return product;
    }
    
    synchronized void produce(String product, int n) {
        while(this.buffer.size() >= this.size) {
            try {
                wait();
            } catch (InterruptedException ex) {

            }
        }
        String[] tbl = {product.charAt(1)+"",product.charAt(3)+"",product.charAt(5)+""+n};
        this.tableH.addRow(tbl);
        this.buffer.add(product);
        this.progB.setValue(this.buffer.size());
        
        notify();
    }
    

    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}