package com.smtmvc.messageService.task;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 延时消息队列借口
 * @author ZHANGYUKUN
 *
 */
public class MessageTasks implements Runnable {
	/**
	 * 延时队列
	 */
	private static  DelayQueue<MessageTask> tasks = new DelayQueue<>();
	
	/**
	 * 执行消息任务的线程池
	 */
	private static ExecutorService messageTackThreadPool = Executors.newFixedThreadPool(10);
	
	
	
	public MessageTasks() {
		new Thread(this).start();;
	}
	

	public static DelayQueue<MessageTask> getTasks() {
		return tasks;
	}

	public void put( MessageTask messageTask) {
		tasks.put(messageTask);
	}
	
	public boolean contains(MessageTask messageTask) {
		return MessageTasks.tasks.contains( messageTask );
	}

	
	@Override
	public void run() {
		do {
			MessageTask messageTask;
			try {
				messageTask = MessageTasks.getTasks().take();
				messageTackThreadPool.execute( messageTask  );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(true);
		
	}
	

}
