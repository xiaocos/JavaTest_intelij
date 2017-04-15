package practice;

public class SingleInstance implements Runnable{
	private static SingleInstance instance = new SingleInstance();
	private SingleInstance(){
		
	}
	
	public static SingleInstance getInstance(){
		if(instance == null){
			instance = new SingleInstance();
		}
		return instance;
	}
	
	public static SingleInstance getInstance2(){
		if(instance == null){
			synchronized (SingleInstance.class) {
				if(null == instance){
					instance = new SingleInstance();
				}
			}
		}
		return instance;
	}

	public static SingleInstance getInstance3(){
		return instance;
	}
	
	public static void main(String args[]){
		SingleInstance si1 = SingleInstance.getInstance();
		SingleInstance.instance = null;
		SingleInstance si2 = SingleInstance.getInstance();
		Thread t1 = new Thread(si1);
		Thread t2 = new Thread(si2);
		t1.start();
		t2.start();
	}
	
	@Override
	public void run() {
		System.out.println("run.."+Thread.currentThread().getId());
		System.out.println(this.hashCode());
	}
}
