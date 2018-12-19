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
import static rexy.module.wexy.actions.endpoint.EndpointObjectInstances.isMock;
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
			Template template = createTemplate("endpoint", crumbBuilder);
			return createResponse(template, api, endpoint, "mock");
		}
		catch (IOException e) {
			throw new RuntimeException("Could perform request", e);
		}
	}
	
	protected RexyResponse createResponse(Template template,
			Api api, Endpoint endpoint, String activeModule) throws IOException {
		return createResponse(template
				.with("tabs", findTabs(api, endpoint, activeModule))
				.with("url", generateUrl(api, endpoint)));
	}
	
	protected List<Tab<Module>> findTabs(Api api, Endpoint endpoint, String activeModule) {
		Set<ObjectInstance> results = mBeanRepo.findForEndpoint(api, endpoint);
		
		return results.stream()
				.filter(EndpointObjectInstances::isModule)
				.map(oi -> createTab(oi, results, endpoint, activeModule))
				.collect(toList());
	}
	
	private Tab<Module> createTab(ObjectInstance moduleObjectInstance,
			Set<ObjectInstance> objectInstances, Endpoint endpoint, String activeModule) {
		
		Tab<Module> tab = createTab(moduleObjectInstance, objectInstances, endpoint);
		
		if (tab.getId().equals(activeModule)) {
			tab.setActive();
		}
		
		return tab;
	}
	
	private Tab<Module> createTab(ObjectInstance moduleObjectInstance,
			Set<ObjectInstance> objectInstances, Endpoint endpoint) {
		
		MBeanInfo info = mBeanRepo.getInfo(moduleObjectInstance);
		List<Input> inputs = stream(info.getAttributes())
				.map(attributeInfo -> createInput(moduleObjectInstance, attributeInfo,
						mBeanRepo.getAttributeValue(moduleObjectInstance, attributeInfo)))
				.collect(toList());
		
		String name = moduleObjectInstance.getObjectName().getKeyProperty("name");
		
		if (isMock(moduleObjectInstance)) {
			return createMockTab(moduleObjectInstance, objectInstances, endpoint, name, info, inputs);
		}
		
		String action = new EndpointLink(endpoint).getLink();
		return new Tab<>(name, new Module(moduleObjectInstance, info, inputs, action));
	}
	
	private Tab<Module> createMockTab(ObjectInstance moduleObjectInstance, Set<ObjectInstance> objectInstances,
			Endpoint endpoint, String name, MBeanInfo info, List<Input> inputs) {
		
		List<PresetLink> presets = objectInstances.stream()
				.filter(EndpointObjectInstances::isPreset)
				.map(oi -> new PresetLink(endpoint, oi))
				.collect(toList());
		
		String action = new EndpointLink(endpoint).getLink();
		MockModule module = new MockModule(moduleObjectInstance, info, inputs, action, presets);
		return new Tab<>(name, module);
	}
	
	private Url generateUrl(Api api, Endpoint endpoint) {
		return Url.fromUrl(baseUrl + api.getBaseUrl() + endpoint.getEndpoint());
	}
	
}