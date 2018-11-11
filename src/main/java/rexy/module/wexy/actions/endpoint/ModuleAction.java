package rexy.module.wexy.actions.endpoint;

import com.github.jknack.handlebars.Handlebars;
import rexy.http.RexyResponse;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder.EndpointCrumbBuilder;
import rexy.module.wexy.mbean.MBeanRepo;
import rexy.module.wexy.model.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static rexy.module.wexy.model.Notification.failure;
import static rexy.module.wexy.model.Notification.success;

public class ModuleAction extends EndpointAction {
	
	private final MBeanRepo mBeanRepo = new MBeanRepo();
	
	public ModuleAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	protected RexyResponse perform(Map<String, String> params,
			EndpointCrumbBuilder crumbBuilder, List<Tab<Module>> tabs) throws IOException {
		
		Template template = createTemplate(crumbBuilder, tabs);
		
		try {
			template.withNotification(success("Yay, it worked!"));
		}
		catch (Exception e) {
			template.withNotification(failure("The action could not be performed"));
		}
		
		return createResponse(template);
	}
	
}