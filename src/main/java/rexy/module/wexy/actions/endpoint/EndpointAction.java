package rexy.module.wexy.actions.endpoint;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.config.model.Endpoint;
import rexy.http.RexyResponse;
import rexy.module.wexy.actions.api.EndpointLink;
import rexy.module.wexy.builders.BreadcrumbBuilder.HomeCrumbBuilder.ApiCrumbBuilder.EndpointCrumbBuilder;
import rexy.module.wexy.mbean.MBeanRepo;
import rexy.module.wexy.model.Template;
import rexy.module.wexy.model.input.Input;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static rexy.module.wexy.actions.endpoint.InputFactory.createInput;

public class EndpointAction extends AbstractEndpointAction {
	
	protected final MBeanRepo mBeanRepo = new MBeanRepo();
	
	public EndpointAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	protected RexyResponse perform(Api api, Endpoint endpoint,
			Map<String, String> params, EndpointCrumbBuilder crumbBuilder) {
		
		try {
			Template template = createTemplate("endpoint", crumbBuilder)
					.with("tabs", findTabs(api, endpoint));
			
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
	protected List<Tab<Module>> findTabs(Api api, Endpoint endpoint) {
		Set<ObjectInstance> results = mBeanRepo.findForEndpoint(api, endpoint);
		
		return results.stream()
				.filter(oi -> oi.getObjectName().getKeyProperty("component") == null)
				.map(oi -> createTab(oi, results, endpoint))
				.collect(toList());
	}
	
	private Tab<Module> createTab(ObjectInstance objectInstance,
			Set<ObjectInstance> objectInstances, Endpoint endpoint) {
		
		String name = objectInstance.getObjectName().getKeyProperty("name");
		
		MBeanInfo info = mBeanRepo.getInfo(objectInstance);
		List<Input> inputs = stream(info.getAttributes())
				.map(mbai -> createInput(objectInstance, mbai, mBeanRepo.getAttributeValue(objectInstance, mbai)))
				.collect(toList());
		
		String action = new EndpointLink(endpoint).getLink();
		
		if (name.equals("mock")) {
			List<PresetLink> presets = objectInstances.stream()
					.filter(oi -> "preset".equals(oi.getObjectName().getKeyProperty("component")))
					.map(oi -> new PresetLink(endpoint, oi))
					.collect(toList());
			
			
			MockModule module = new MockModule(objectInstance, info, inputs, action, presets);
			Tab<Module> tab = new Tab<>(name, module);
			tab.setActive();
			return tab;
		}
		
		return new Tab<>(name, new Module(objectInstance, info, inputs, action));
	}
	
}