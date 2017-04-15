package pattern.mediator;

public abstract class Chanel {
	private Mediator mediator;
	public Chanel(Mediator mediator){
		this.mediator = mediator;
	}
	public Mediator getMediator() {
		return mediator;
	}
	public abstract void sendMsg(Object msg);
	
}
