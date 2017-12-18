package com.smtmvc.messageService.task;

import java.util.concurrent.DelayQueue;


public class ReSendTasks implements Runnable {
	
	public ReSendTasks() {
		new Thread(this).start();;
	}
	
	private static  DelayQueue<ReSendTask> tasks = new DelayQueue<>();

	public static DelayQueue<ReSendTask> getTasks() {
		return tasks;
	}

	public void put( ReSendTask reSendTask) {
		tasks.put(reSendTask);
	}
	
	public boolean contains(ReSendTask reSendTask) {
		return ReSendTasks.tasks.contains( reSendTask );
	}

	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 暂时就这么写,后面换成线程池
	 */
	@Override
	public void run() {
		do {
			ReSendTask reSendTask;
			try {
				reSendTask = ReSendTasks.getTasks().take();
				reSendTask.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(true);
		
	}
	

}
