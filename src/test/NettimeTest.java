package test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NettimeTest {
	public static void main(String[] args) throws IOException  {
		getNetTime();
		
	}
	
	public static String getNetTime() throws IOException{
		URL url = new URL("http://www.baidu.com");
		URLConnection uc = url.openConnection();
		uc.connect();
		long id = uc.getDate();
		Date date = new Date(id);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH-mm-ss");
		String nowTime = format.format(date);
		System.out.println("nowTime:"+nowTime);
		return nowTime;
	}
}
