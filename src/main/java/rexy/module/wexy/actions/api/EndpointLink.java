package rexy.module.wexy.actions.api;

import rexy.config.model.Endpoint;

import static rexy.module.wexy.Utils.toUrl;
import static rexy.utils.Paths.join;

public class EndpointLink {
	private final Endpoint endpoint;
	private final String link;
	
	public EndpointLink(Endpoint endpoint) {
		this.endpoint = endpoint;
		this.link = join("/", endpoint.getApi().getBaseUrl(), toUrl(endpoint));
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public String getLink() {
		return link;
	}

}