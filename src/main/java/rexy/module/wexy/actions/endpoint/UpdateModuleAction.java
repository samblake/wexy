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

public class UpdateModuleAction extends EndpointAction {
	
	private final MBeanRepo mBeanRepo = new MBeanRepo();
	
	public UpdateModuleAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	protected RexyResponse perform(Map<String, String> params,
			EndpointCrumbBuilder crumbBuilder, List<Tab<Module>> tabs) throws IOException {
		
		String moduleName = params.get("module");
		String presetName = params.get("preset");
		
		Template template = createTemplate(crumbBuilder, tabs);
		
		if (presetName != null) {
			applyPreset(moduleName, presetName, template);
		}
		else {
			applyUpdate(moduleName, params, template);
		}
		
		return createResponse(template);
	}
	
	private void applyPreset(String name, String presetName, Template template) {
		try {
			template.withNotification(success("The %s preset has been applied", presetName));
		}
		catch (Exception e) {
			template.withNotification(failure("The %s preset could not be applied", presetName));
		}
	}
	
	private void applyUpdate(String moduleName, Map<String, String> params, Template template) {
		try {
			template.withNotification(success("The %s module has been updated", moduleName));
		}
		catch (Exception e) {
			template.withNotification(failure("The %s module could not be updated", moduleName));
		}
	}
	
}