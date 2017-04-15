package com.compute.thread;

public class Thread_dataService extends Thread {
	private int count = 0;

	@Override
	public void run() {
		super.run();
		while (true) {
			Thread_sendAndRecieve t = Thread_sendAndRecieve.getInstance();
			synchronized (t) {
				System.out.println(" !t.isAlive()"+!t.isAlive()
						+" t.isInterrupted()"+t.isInterrupted()
						+" time"+System.currentTimeMillis()/1000);
			if(!t.isAlive()){
				System.out.println("t.start.."+" time"+System.currentTimeMillis()/1000);
				t.start();
			}
				System.out.println("t.notify.."+" time"+System.currentTimeMillis()/1000);
				t.notify();
//			System.out.println("Thread_dataService "
//					+ Thread.currentThread().getId() + " "
//					+ Thread.currentThread().getName());
//			try {
//					wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
