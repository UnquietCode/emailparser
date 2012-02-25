package temp;

import java.io.File;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MboxHandler handler = new MboxHandler(new File(
				"D:\\Desktop\\mbox\\testMbox.mbox"));
		System.out.println(handler.toString());
	}

}
