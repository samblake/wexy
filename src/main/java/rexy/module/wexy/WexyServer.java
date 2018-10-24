package rexy.module.wexy;

import com.github.jknack.handlebars.Handlebars;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rexy.http.NanoRequest;
import rexy.http.RexyHandler;
import rexy.http.RexyServer;
import rexy.module.Module;
import rexy.module.wexy.actions.IndexAction;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status.OK;
import static rexy.http.Method.GET;

public class WexyServer extends RexyServer {
	private static final Logger logger = LogManager.getLogger(WexyServer.class);
	
	private static final String MIME_HTML = "text/html";
	
	private final String baseUrl;
	private final IndexAction indexAction;
	private final Handlebars handlebars;
	
	public WexyServer(int port, String baseUrl, List<Module> modules) {
		super(port, baseUrl, modules);
		this.baseUrl = baseUrl;
		this.handlebars = new Handlebars();
		
		indexAction = new IndexAction(baseUrl, handlebars, routes);
	}
	
	public Map<String, RexyHandler> getRoutes() {
		return routes;
	}
	
	public Handlebars getHandlebars() {
		return handlebars;
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String url = session.getUri(); // TODO remove with base URL
		
		// Handle static files
		if (url.endsWith(".css") || url.endsWith(".ljs")) {
			InputStream resource = session.getClass().getResourceAsStream(url);
			if (resource != null) {
				logger.debug("Serving " + url);
				String mineType = url.endsWith(".css") ? "text/css" : "application/javascript";
				return newChunkedResponse(OK, mineType, resource);
			}
		}
		
		// Handle index
		if (url.equalsIgnoreCase(baseUrl)) {
			NanoRequest request = new NanoRequest(session, "/");
			if (request.getMethod() == GET) {
				return createRespone(indexAction.perform());
			}
		}
		
		// All other business
		return super.serve(session);
	}
	
}