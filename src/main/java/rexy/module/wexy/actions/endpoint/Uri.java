package rexy.module.wexy.actions.endpoint;

import rexy.http.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uri {
	private static final Pattern PATTERN = Pattern.compile("\\{(.+?)}");
	
	private final Method method;
	private final String template;
	private final List<String> parameters;
	
	private Uri(Method method, String template, List<String> parameters) {
		this.method = method;
		this.template = template;
		this.parameters = parameters;
	}
	
	public String getMethod() {
		return method.name();
	}
	
	public String getTemplate() {
		return template;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	
	public boolean isParameterised() {
		return !parameters.isEmpty();
	}
	
	public String getPath() {
		return template.indexOf('?') == -1 ? template : template.substring(0, template.indexOf('?'));
	}
	
	public static Uri fromUrl(Method method, String url) {
		Matcher matcher = PATTERN.matcher(url);
		List<String> parameters = new ArrayList<>();
		while (matcher.find()) {
			parameters.add(matcher.group(1));
		}
		return new Uri(method, url, parameters);
	}

}