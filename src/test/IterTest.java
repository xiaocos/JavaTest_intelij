package test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**迭代器的使用*/
public class IterTest {
	public static void main(String args[]){
		List list = new ArrayList<String>();
		list.add("aa");
		list.add("bb");
		list.add("cc");
		list.add("dd");
		list.add("ee");
		Iterator<String> it = list.iterator();
		int count =0;
		while(it.hasNext()){
			count++;
			String str = new String();
			str =it.next();
			System.out.println();
			System.out.println(count);
		}
	}
}
