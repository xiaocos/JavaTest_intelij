package pattern.mediator;

public class Chanel1 extends Chanel {

	public Chanel1(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void sendMsg(Object msg) {
		System.out.println("chanel1 send msg to the MainServer.");
		System.out.println("The message is:"+msg.toString());
	}

}
