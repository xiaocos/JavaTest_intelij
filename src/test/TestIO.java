package test;
import java.io.File;

public class TestIO {
	public static void main(String[] args) {
		File f = new File("E:\\z.txt");
		boolean i = f.exists();
		if (i) {
			System.out.println("z.txt exists");
		} else {
			System.out.println("z.txt is not exists");
		}
	}
}
