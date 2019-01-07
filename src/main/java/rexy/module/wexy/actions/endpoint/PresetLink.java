package rexy.module.wexy.actions.endpoint;

import rexy.config.model.Endpoint;

import javax.management.ObjectInstance;

import static rexy.module.wexy.Utils.toUrl;
import static rexy.utils.Paths.join;

public class PresetLink {
	private final Endpoint endpoint;
	private final ObjectInstance preset;
	private final String name;
	private final String link;
	
	public PresetLink(Endpoint endpoint, ObjectInstance preset) {
		this.endpoint = endpoint;
		this.preset = preset;
		this.name = preset.getObjectName().getKeyProperty("name");
		this.link = '/' + join(endpoint.getApi().getBaseUrl(), toUrl(endpoint));
	}
	
	public String getName() {
		return name;
	}
	
	public String getLink() {
		return link;
	}
	
}