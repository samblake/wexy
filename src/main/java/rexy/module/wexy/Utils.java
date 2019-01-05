package rexy.module.wexy;

public class Utils {

	private Utils() {
	}
	
	public static String trim(String value, int start, int end) {
		return value.substring(start, value.length()-end);
	}
	
	public static String until(String value, char c) {
		int idx = value.indexOf(c);
		return idx == -1 ? value : value.substring(0, idx);
	}
	
}
