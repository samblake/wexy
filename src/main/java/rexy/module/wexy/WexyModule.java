package rexy.module.wexy;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Handlebars;
import jauter.Routed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rexy.config.model.Api;
import rexy.http.RexyHandler;
import rexy.http.RexyRequest;
import rexy.http.RexyResponse;
import rexy.module.ModuleAdapter;
import rexy.module.wexy.actions.WexyAction;
import rexy.module.wexy.actions.api.ApiAction;
import rexy.module.wexy.actions.endpoint.EndpointAction;
import rexy.module.wexy.actions.module.UpdateModuleAction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static rexy.http.Method.GET;
import static rexy.http.Method.POST;
import static rexy.utils.Json.intValue;
import static rexy.utils.Json.stringValue;

public class WexyModule extends ModuleAdapter {
	private static final Logger logger = LogManager.getLogger(WexyModule.class);
	
	private WexyServer wexyServer;
	private String baseUrl;
	
	@Override
	public void init(JsonNode config) {
		this.baseUrl = stringValue(config, "baseUrl", "/");
		Integer port = intValue(config, "port", 8090);
		InternalModule internalModule = new InternalModule();
		this.wexyServer = new WexyServer(port, baseUrl, singletonList(internalModule));
		internalModule.init(config);
		
		try {
			wexyServer.start();
			logger.info("Wexy starterd on port " + port);
		}
		catch (IOException e) {
			throw new RuntimeException("Could not start Wexy server", e);
		}
	}
	
	@Override
	public void initEndpoint(Api api) {
		wexyServer.initApi(api);
	}
	
	@Override
	public Optional<RexyResponse> handleRequest(Api api, RexyRequest request) {
		return Optional.empty();
	}
	
	private final class InternalModule extends ModuleAdapter {
		
		private final WexyRouter router = new WexyRouter();
		
		@Override
		public void init(JsonNode config) {
			Handlebars handlebars = wexyServer.getHandlebars();
			Map<String, RexyHandler> apiRoutes = wexyServer.getRoutes();
			
			router.pattern(GET, "/:api", new ApiAction(baseUrl, handlebars, apiRoutes));
			router.pattern(GET, "/:api/:endpoint", new EndpointAction(baseUrl, handlebars));
			router.pattern(POST, "/:api/:endpoint", new UpdateModuleAction(baseUrl, handlebars));
		}
		
		@Override
		public Optional<RexyResponse> handleRequest(Api api, RexyRequest request) {
			Routed<WexyAction> route = router.route(request.getMethod(), request.getUri());
			
			if (route.notFound()) {
				throw new RuntimeException("No route found for " + request.getUri());
			}
			else {
				Map<String, String> params = combineParams(request, route);
				return Optional.of(route.target().perform(api, params));
			}
		}
	}
	
	private Map<String, String> combineParams(RexyRequest request, Routed<WexyAction> route) {
		Map<String, String> params = new HashMap<>();
		params.putAll(route.params());
		params.putAll(request.getParameters());
		return params;
	}
	
}