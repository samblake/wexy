package rexy.module.wexy.mbean.converters;

import java.util.HashMap;
import java.util.Map;

public class MapConverter implements TypeConverter<Map<Object, Object>> {
	
	@Override
	public boolean handles(String type) {
		return type.equals(Map.class.getName());
	}
	
	@Override
	public Map<Object, Object> convert(String value, String type) {
		// FIXME encode maps in a better way than this
		Map<Object, Object> map = new HashMap<>();
		if (value != null && value.contains("=")) {
			String[] entries = value.substring(1, value.length() - 1).split(",");
			for (String entry : entries) {
				String[] pair = entry.trim().split("=");
				map.put(pair[0], pair[1]);
			}
		}
		return map;
	}
}
