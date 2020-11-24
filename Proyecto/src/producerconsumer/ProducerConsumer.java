package producerconsumer;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class ProducerConsumer {
    
    boolean running_Threads;
    
    public ProducerConsumer() throws InterruptedException{
        this.running_Threads = false;
        run_Frame();
    }

    private static GUIFrame create_frame() {
        GUIFrame guiFrame = new GUIFrame();
        guiFrame.setLocationRelativeTo(null);
        guiFrame.setResizable(false);
        guiFrame.pack();
        guiFrame.setVisible(true);

        return guiFrame;
    }

    private static void call_pane(String text){
        JOptionPane.showMessageDialog(
            null,
            text
        );
    }

    private static void start(
            ArrayList<Producer> producers,
            ArrayList<Consumer> consumers,
            Buffer buffer,
            int n_P,
            int n_C,
            int n,
            int m,
            int break_P,
            int break_C)
    {
        Producer producer;
        Consumer consumer;
        
        for(int i = 0; i < n_P; i++){
            producer = new Producer(buffer, break_P, n, m, i + 1);
            producers.add(producer);
            producer.start();
        }
        
        for(int j = 0; j < n_C; j++){
            consumer = new Consumer(buffer, break_C, j + 1);
            consumers.add(consumer);
            consumer.start();
        }
        
    }
    
    private static void stop(
            ArrayList<Producer> producers,
            ArrayList<Consumer> consumers)
    {
        while (!producers.isEmpty()) {
            producers.get(0).interrupt();
            producers.get(0).prendido = false;
            producers.remove(0);
        }
        while (!consumers.isEmpty()) {
            consumers.get(0).interrupt();
            consumers.get(0).prendido = false;
            consumers.remove(0);
        }
    }
    
    private void run_Frame() throws InterruptedException{
        
        GUIFrame guiFrame = ProducerConsumer.create_frame();
        boolean frame_running = true;
        
        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();
        boolean threads_running = false;
        
        while (frame_running) {
            if (guiFrame.getState() == 1) {
                try {
                    DefaultTableModel makeTable = guiFrame.getMakeTable();
                    DefaultTableModel doneTable = guiFrame.getDoneTable();
                    JLabel doneLable = guiFrame.getDoneLabel();
                    Buffer buffer = new Buffer(
                            guiFrame.getBufferSize(),
                            guiFrame.getProgressBar(),
                            makeTable, doneTable,
                            doneLable
                    );
               
                    int timeout_producer = guiFrame.getProducerWaitTime();
                    int timeout_consumer = guiFrame.getConsumerWaitTime();
                    int nProducers = guiFrame.getProducers();
                    int nConsumers = guiFrame.getConsumers();
                    int n = guiFrame.get_n_value();
                    int m = guiFrame.get_m_value();
                    
                    if (n >= m) {
                        call_pane("M must be less that N");
                        guiFrame.setState(0);
                    }
                    
                    if (guiFrame.getState() == 1 && !this.running_Threads){
                        guiFrame.setDefault();
                        start(
                            producers,
                            consumers,
                            buffer,
                            nProducers,
                            nConsumers,
                            n,
                            m,
                            timeout_producer,
                            timeout_consumer
                        );
                        this.running_Threads = !this.running_Threads;
                        if(guiFrame.getState() != 1)
                            break;
                    }
                    
                } catch (NumberFormatException e) {
                    frame_running = true;
                    call_pane("Error: invalid number: " + e);
                    guiFrame.setState(0);
                    
                } catch (Exception ex) {
                    call_pane("EX  " + ex);
                    guiFrame.setState(0);
                }
            }
            if (guiFrame.getState() == -1) {
                stop(
                    producers,
                    consumers
                );
                guiFrame.setState(0);
                this.running_Threads = false;
            }
            Thread.sleep(200);
        }
    }

    public static void main(String[] args) throws InterruptedException{
        ProducerConsumer init = new ProducerConsumer();
    }
}