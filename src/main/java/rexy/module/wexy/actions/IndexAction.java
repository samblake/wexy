package rexy.module.wexy.actions;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.http.RexyHandler;
import rexy.http.RexyResponse;
import rexy.module.wexy.model.Template;

import java.io.IOException;
import java.util.Map;

public class IndexAction extends AbstractApiAction {
	
	public IndexAction(String baseUrl, Handlebars handlebars, Map<String, RexyHandler> routes) {
		super(baseUrl, handlebars, routes);
	}
	
	@Override
	public RexyResponse perform(Api api, Map<String, String> params) {
		return perform();
	}
	
	public RexyResponse perform() {
		try {
			Template template = createTemplate("index", createBreadcrumbs())
					.with("apis", getApiLinks());
			
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
}