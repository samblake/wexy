package rexy.module.wexy.actions.endpoint;

import com.github.jknack.handlebars.Handlebars;
import rexy.config.model.Api;
import rexy.http.RexyResponse;
import rexy.module.wexy.actions.WexyAction;
import rexy.module.wexy.builders.BreadcrumbBuilder;
import rexy.module.wexy.mbean.MBeanQueryBuilder;
import rexy.module.wexy.mbean.MBeanRepo;
import rexy.module.wexy.mbean.RexyQueryBuilder;
import rexy.module.wexy.model.Template;

import javax.management.ObjectInstance;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

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
			List<Tab<Module>> tabs = objectInstances.stream()
					.filter(oi -> oi.getObjectName().getKeyProperty("component") == null)
					.map(oi -> createTab(oi, objectInstances))
					.collect(toList());
			
			BreadcrumbBuilder breadcrumbs = createBreadcrumbs().withApi(api).withEndpoint(endpointName);
			Template template = createTemplate("endpoint", breadcrumbs).with("tabs", tabs);
			return createResponse(template);
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
	private Tab<Module> createTab(ObjectInstance objectInstance, Set<ObjectInstance> objectInstances) {
		String name = objectInstance.getObjectName().getKeyProperty("name");
		
		if (name.equals("mock")) {
			List<ObjectInstance> presets = objectInstances.stream()
					.filter(oi -> "preset".equals(oi.getObjectName().getKeyProperty("component")))
					.collect(toList());
			
			MockModule module = new MockModule(objectInstance, mBeanRepo.getInfo(objectInstance), presets);
			Tab<Module> tab = new Tab<>(name, module);
			tab.setActive();
			return tab;
		}

		return new Tab<>(name, new Module(objectInstance, mBeanRepo.getInfo(objectInstance)));
	}
	
}