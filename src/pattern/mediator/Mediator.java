package pattern.mediator;
/**
 * 中介者模式（Mediator）
 * */
public interface Mediator {
	public void createMediator();  
    public void workAll(); 
    public void sendMsg(Object msg);
    public void sendMsg();
}
