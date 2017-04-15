package com.compute.thread;

public class Thread_sendAndRecieve extends Thread{
	private int count = 0;
	private String str = "";
	private static boolean iswait = false;
	private static Thread_sendAndRecieve instance = new Thread_sendAndRecieve();
	
	private Thread_sendAndRecieve(){
		
	}
	
	public static Thread_sendAndRecieve getInstance(){
		System.out.println("instance "+instance.getId()
				+" "+instance.getName());
		return instance;
	};
	public static boolean isWait(){
		return iswait;
	}
	@Override
	public void run() {
		super.run();
		while(true){
			count++;
			try {
				synchronized (instance) {
//					System.out.println("Thread_sendAndRecieve "+Thread.currentThread().getId()
//							+" " + Thread.currentThread().getName());
					System.out.println(" ++++count:"+count
							+" instance.isInterrupted():"+instance.isInterrupted()
							+" instance.isAlive():"+instance.isAlive()
							+" instance.isDaemon():"+instance.isDaemon());
					if(!iswait){
						System.out.println(" instance.wait()");
						instance.wait();
						iswait = true;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
