package rexy.module.wexy.actions.endpoint;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.config.model.Endpoint;
import rexy.http.RexyResponse;
import rexy.module.wexy.actions.WexyAction;
import rexy.module.wexy.builders.BreadcrumbBuilder;
import rexy.module.wexy.mbean.MBeanQueryBuilder;
import rexy.module.wexy.mbean.MBeanRepo;
import rexy.module.wexy.mbean.RexyQueryBuilder;
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

public class EndpointAction extends WexyAction {
	
	private final MBeanRepo mBeanRepo = new MBeanRepo();
	
	public EndpointAction(String baseUrl, Handlebars handlebars) {
		super(baseUrl, handlebars);
	}
	
	@Override
	public RexyResponse perform(Api api, Map<String, String> params) {
		String endpoint = params.get("endpoint");
		
		MBeanQueryBuilder query = new RexyQueryBuilder().withApi(api.getName()).withScope(endpoint);
		Set<ObjectInstance> results = mBeanRepo.search(query);
		return perform(api, endpoint, results);
	}
	
	private RexyResponse perform(Api api, String endpointName, Set<ObjectInstance> objectInstances) {
		try {
			Endpoint endpoint = api.getEndpoints().stream()
					.filter(e -> e.getName().equals(endpointName))
					.findFirst().orElseThrow(() -> new RuntimeException("Unknown endpoint " + endpointName));
			
			List<Tab<Module>> tabs = objectInstances.stream()
					.filter(oi -> oi.getObjectName().getKeyProperty("component") == null)
					.map(oi -> createTab(oi, objectInstances, endpoint))
					.collect(toList());
			
			BreadcrumbBuilder breadcrumbs = createBreadcrumbs().withApi(api).withEndpoint(endpoint);
			Template template = createTemplate("endpoint", breadcrumbs).with("tabs", tabs);
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
	private Tab<Module> createTab(ObjectInstance objectInstance,
			Set<ObjectInstance> objectInstances, Endpoint endpoint) {
		
		String name = objectInstance.getObjectName().getKeyProperty("name");
		
		MBeanInfo info = mBeanRepo.getInfo(objectInstance);
		List<Input> inputs = stream(info.getAttributes())
				.map(mbai -> createInput(objectInstance, mbai, mBeanRepo.getAttributeValue(objectInstance, mbai)))
				.collect(toList());
		
		if (name.equals("mock")) {
			List<PresetLink> presets = objectInstances.stream()
					.filter(oi -> "preset".equals(oi.getObjectName().getKeyProperty("component")))
					.map(oi -> new PresetLink(endpoint, oi))
					.collect(toList());
			
			MockModule module = new MockModule(objectInstance, info, inputs, presets);
			Tab<Module> tab = new Tab<>(name, module);
			tab.setActive();
			return tab;
		}
		
		return new Tab<>(name, new Module(objectInstance, info, inputs));
	}
	
}