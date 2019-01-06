package rexy.module.wexy.actions.api;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.http.response.RexyResponse;
import rexy.module.wexy.actions.WexyAction;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder;

import java.util.Map;

public abstract class AbstractApiAction extends WexyAction {
	
	public AbstractApiAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	public RexyResponse perform(Api api, Map<String, String> params) {
		return perform(api, params, createBreadcrumbs().withApi(api));
	}
	
	protected abstract RexyResponse perform(Api api, Map<String, String> params, ApiCrumbBuilder crumbBuilder);
	
}