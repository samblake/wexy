package rexy.module.wexy.actions;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.http.RexyResponse;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder;
import rexy.module.wexy.builders.Builder;
import rexy.module.wexy.model.Breadcrumbs;
import rexy.module.wexy.model.Template;

import java.io.IOException;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.MIME_HTML;
import static java.util.Collections.emptyMap;

public abstract class WexyAction {
	
	protected final String baseUrl;
	private final Handlebars handlebars;
	
	public WexyAction(String baseUrl, Handlebars handlebars) {
		this.baseUrl = baseUrl;
		this.handlebars = handlebars;
	}
	
	public abstract RexyResponse perform(Api api, Map<String, String> params);
	
	protected RexyResponse createResponse(Template template) throws IOException {
		byte[] body = template.apply().getBytes();
		return new RexyResponse(200, emptyMap(), MIME_HTML, body);
	}
	
	protected Template createTemplate(String name, Builder<Breadcrumbs> breadcrumbs) throws IOException {
		return new Template(handlebars, name).with("crumbs", breadcrumbs.build());
	}
	
	protected HomeCrumbBuilder createBreadcrumbs() {
		return new HomeCrumbBuilder(baseUrl);
	}
	
}