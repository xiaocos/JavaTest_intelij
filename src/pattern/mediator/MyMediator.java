package pattern.mediator;

public class MyMediator implements Mediator {
	private User user1;  
    private User user2;
    private Chanel chanel1;
    private Chanel chanel2;
    
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@Override
	public void createMediator() {
		user1 = new User1(this);
        user2 = new User2(this);
        chanel1 = new Chanel1(this);
        chanel2 = new Chanel2(this);
	}

	@Override
	public void workAll() {
		user1.work();
        user2.work();
	}
	

	@Override
	public void sendMsg(Object msg) {

	}

	@Override
	public void sendMsg() {
		chanel1.sendMsg(new String("Just give me the file please."));
		chanel2.sendMsg(new String("Just give me the safekey,or i will try again."));
	}

}
