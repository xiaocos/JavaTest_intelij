package pattern.mediator;

public class MediatorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mediator mediator = new MyMediator();
		mediator.createMediator();
		mediator.workAll();
		mediator.sendMsg();
	}

}
