package pattern.mediator;
/**
 * �н���ģʽ��Mediator��
 * */
public interface Mediator {
	public void createMediator();  
    public void workAll(); 
    public void sendMsg(Object msg);
    public void sendMsg();
}
