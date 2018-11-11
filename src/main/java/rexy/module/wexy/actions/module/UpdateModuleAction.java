package rexy.module.wexy.actions.module;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Endpoint;
import rexy.http.RexyResponse;
import rexy.module.wexy.actions.endpoint.EndpointAction;
import rexy.module.wexy.actions.endpoint.Module;
import rexy.module.wexy.actions.endpoint.Tab;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder.EndpointCrumbBuilder;
import rexy.module.wexy.model.Template;

import javax.management.JMException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static rexy.module.wexy.model.Notification.failure;
import static rexy.module.wexy.model.Notification.success;

public class UpdateModuleAction extends EndpointAction {
	
	public UpdateModuleAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	protected RexyResponse perform(Endpoint endpoint, Map<String, String> params,
			EndpointCrumbBuilder crumbBuilder, List<Tab<Module>> tabs) throws IOException {
		
		String moduleName = params.get("module");
		String presetName = params.get("preset");
		
		Template template = createTemplate(crumbBuilder, tabs);
		
		if (presetName != null) {
			applyPreset(endpoint, presetName, template);
		}
		else {
			applyUpdate(params, moduleName, template);
		}
		
		return createResponse(template);
	}
	
	private void applyPreset(Endpoint endpoint, String presetName, Template template) {
		try {
			mBeanRepo.applyPreset(endpoint, presetName);
			
			template.withNotification(success("The %s preset has been applied", presetName));
		}
		catch (JMException e) {
			template.withNotification(failure("The %s preset could not be applied: %s", presetName, e.getMessage()));
		}
		catch (Exception e) {
			template.withNotification(failure("The %s preset could not be applied", presetName));
		}
	}
	
	private void applyUpdate(Map<String, String> params, String moduleName, Template template) {
		try {
			// TODO actually update the MBean
			
			template.withNotification(success("The %s module has been updated", moduleName));
		}
		//catch (JMException e) {
		//	template.withNotification(failure("The %s module could not be updated: %s", moduleName, e.getMessage()));
		//}
		catch (Exception e) {
			template.withNotification(failure("The %s module could not be updated", moduleName));
		}
	}
	
}