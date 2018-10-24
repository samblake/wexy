package rexy.module.wexy.actions;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.config.model.Endpoint;
import rexy.http.RexyResponse;
import rexy.module.wexy.model.Template;

import java.io.IOException;
import java.util.Map;

public class EndpointAction extends WexyAction {
	
	public EndpointAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	public RexyResponse perform(Api api, Map<String, String> params) {
		String endpointId = params.get("endpoint");
		return perform(api, (Endpoint)null);
	}
	
	private RexyResponse perform(Api api, Endpoint endpoint) {
		/*
		try {
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(String.format("Rexy:type=%s,*", api.getName()));
			Set<ObjectInstance> objectInstances = server.queryMBeans(objectName, null);
			return of(endpoints(api));
		}
		catch (MalformedObjectNameException e) {
			return of(errorResponse(500, e.getMessage()));
		}
		*/
		
		try {
			Template template = createTemplate("endpoint", createBreadcrumbs().withApi(api));
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}

}