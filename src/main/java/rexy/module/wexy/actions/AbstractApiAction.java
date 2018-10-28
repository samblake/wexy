package rexy.module.wexy.actions;

import com.github.jknack.handlebars.Handlebars;
import rexy.http.RexyHandler;
import rexy.module.wexy.actions.index.ApiLink;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public abstract class AbstractApiAction extends WexyAction {
	
	private final Map<String, RexyHandler> routes;
	
	public AbstractApiAction(String baseUrl, Handlebars handlebars, Map<String, RexyHandler> routes) {
		super(baseUrl, handlebars);
		this.routes = routes;
	}
	
	public List<ApiLink> getApiLinks() {
		return routes.entrySet().stream()
				.map(e -> new ApiLink(e.getValue().getApi(), e.getKey()))
				.collect(toList());
	}
	
}