package rexy.module.wexy.actions.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static rexy.module.wexy.Utils.trim;

public class Url {
	private static final Pattern PATTERN = Pattern.compile("\\{.+?}");
	
	private final String template;
	private final List<String> parameters;
	
	private Url(String template, List<String> parameters) {
		this.template = template;
		this.parameters = parameters;
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
	
	public static Url fromUrl(String url) {
		Matcher matcher = PATTERN.matcher(url);
		List<String> parameters = new ArrayList<>();
		while (matcher.find()) {
			parameters.add(trim(matcher.group(), 1, 1));
		}
		return new Url(url, parameters);
	}

}