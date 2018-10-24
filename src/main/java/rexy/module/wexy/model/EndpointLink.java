package rexy.module.wexy.model;

import rexy.config.model.Endpoint;

import static rexy.utils.Paths.join;

public class EndpointLink {
	private final Endpoint endpoint;
	private final String link;
	
	public EndpointLink(Endpoint endpoint) {
		this.endpoint = endpoint;
		this.link = join(endpoint.getApi().getBaseUrl(), endpoint.getName());
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public String getLink() {
		return link;
	}

}