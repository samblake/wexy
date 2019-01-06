package rexy.module.wexy;

public class Utils {

	private Utils() {
	}
	
	public static String until(String value, char c) {
		int idx = value.indexOf(c);
		return idx == -1 ? value : value.substring(0, idx);
	}
	
}
