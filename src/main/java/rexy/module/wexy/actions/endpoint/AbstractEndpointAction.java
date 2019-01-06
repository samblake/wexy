package rexy.module.wexy.actions.endpoint;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.config.model.Endpoint;
import rexy.http.response.RexyResponse;
import rexy.module.wexy.actions.api.AbstractApiAction;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder.EndpointCrumbBuilder;
import rexy.module.wexy.mbean.MBeanRepo;

import java.util.Map;

public abstract class AbstractEndpointAction extends AbstractApiAction {
	
	private final MBeanRepo mBeanRepo = new MBeanRepo();
	
	public AbstractEndpointAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	protected RexyResponse perform(Api api, Map<String, String> params, ApiCrumbBuilder crumbBuilder) {
		String endpointName = params.get("endpoint");
		
		Endpoint endpoint = api.getEndpoints().stream()
				.filter(e -> e.getName().equals(endpointName))
				.findFirst().orElseThrow(() -> new RuntimeException("Unknown endpoint " + endpointName));
		
		return perform(api, endpoint, params, crumbBuilder.withEndpoint(endpoint));
	}
	
	protected abstract RexyResponse perform(Api api, Endpoint endpoint,
			Map<String, String> params, EndpointCrumbBuilder crumbBuilder);
	
}