package mediaTracker;

import java.io.File;
import java.io.IOException;

public class Program {
	
//	private static String getPath(File f) {
//		String str = f.getAbsolutePath();
//		return str.substring(0,str.lastIndexOf(f.getName()));
//	}

	public static void main(String[] args) throws IOException {
		Window win = new Window(new File("").getAbsolutePath());
	}

}
