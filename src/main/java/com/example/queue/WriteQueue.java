package com.example.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author bhrigu
 *
 */

public class WriteQueue {

	private LinkedList<String> queue;
    private final Semaphore writer = new Semaphore(1);
     
    /**
     * Initialize values
     * @param queue
     * @return 
     */
    public WriteQueue(LinkedList<String> queue) {
        this.queue = queue;
    }
     
    /**
     * @param item
     */
    public void put(final String item) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                
                    try {
                        writer.acquire();
                        queue.add(item);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        writer.release(1);
                    }
                }
            }).start();
    }
    
    /**
     * @param list of items
     */
    public void put(final List<String> items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            
                try {
                    writer.acquire();
                    queue.addAll(items);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    writer.release(1);
                }
            }
        }).start();
}
}
