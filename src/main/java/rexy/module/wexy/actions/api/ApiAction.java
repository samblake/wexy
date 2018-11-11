package rexy.module.wexy.actions.api;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.http.RexyHandler;
import rexy.http.RexyResponse;
import rexy.module.wexy.actions.index.ApiLink;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder;
import rexy.module.wexy.model.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ApiAction extends AbstractApiAction {
	
	private final Map<String, RexyHandler> routes;
	
	public ApiAction(String baseUrl, Handlebars handlebars, Map<String, RexyHandler> routes) {
		super(baseUrl, handlebars);
		this.routes = routes;
	}
	
	@Override
	public RexyResponse perform(Api api, Map<String, String> params, ApiCrumbBuilder crumbBuilder) {
		List<EndpointLink> endpoints = api.getEndpoints().stream().map(EndpointLink::new).collect(toList());
		
		try {
			Template template = createTemplate("api", crumbBuilder)
					.with("apis", ApiLink.fromRoutes(routes))
					.with("endpoints", endpoints);
			
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
}