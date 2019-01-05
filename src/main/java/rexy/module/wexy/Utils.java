package rexy.module.wexy;

public class Utils {

	private Utils() {
	}
	
	public static String trim(String value, int start, int end) {
		return value.substring(start, value.length()-end);
	}
	
}
