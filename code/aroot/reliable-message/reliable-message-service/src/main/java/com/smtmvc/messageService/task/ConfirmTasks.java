package com.smtmvc.messageService.task;

import java.util.concurrent.DelayQueue;

public class ConfirmTasks implements Runnable {
	
	private static  DelayQueue<ConfirmTask> tasks = new DelayQueue<>();

	public static DelayQueue<ConfirmTask> getTasks() {
		return tasks;
	}

	public static void setTasks(DelayQueue<ConfirmTask> tasks) {
		ConfirmTasks.tasks = tasks;
	}

	
	/**
	 * 
	 * 暂时就这么些,后面换成线程池
	 */
	@Override
	public void run() {
		do {
			ConfirmTask confirmTask;
			try {
				confirmTask = ConfirmTasks.getTasks().take();
				confirmTask.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(true);
		
	}

	
	
	
	
	

}
