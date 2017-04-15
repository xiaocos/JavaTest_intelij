package pattern.mediator;

public class Chanel2 extends Chanel {

	public Chanel2(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void sendMsg(Object msg) {
		System.out.println("chanel2 send msg to SafeModule");
		System.out.println("The message is:"+msg.toString());
	}

}
