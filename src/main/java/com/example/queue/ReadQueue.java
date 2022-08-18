package com.example.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;


/**
 * @author bhrigu
 *
 */
public class ReadQueue {
	
	private LinkedList<String> queue = new LinkedList<String>();
    private final Semaphore reader = new Semaphore(1);
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * Initialize values
     * @param queue
     * @return 
     */
    public ReadQueue(LinkedList<String> queue) {
        this.queue = queue;
    }
     
	public String get() throws InterruptedException, ExecutionException {
		Future<String> future = executor.submit(new Callable<String>() {
			@Override
			public String call() {
				String item = null;
				while (!queue.isEmpty()) {
					try {
						reader.acquire(1);
						item = queue.remove();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						reader.release(1);
					}
					return item;
				}
				return item;
				
			}
		});
		return future.get();
    }
	public List<String> getAll() throws InterruptedException, ExecutionException {
		Future<List<String>> future = executor.submit(new Callable<List<String>>() {
			List<String> items = new ArrayList<>();
			@Override
			public List<String> call() {
				
				while (!queue.isEmpty()) {
					try {
						reader.acquire(1);
						items.add(queue.remove());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						reader.release(1);
					}
				}
				return items;
				
			}
		});
		return future.get();
    }
 
    public void shutdown() {
        executor.shutdown();
    }
}
