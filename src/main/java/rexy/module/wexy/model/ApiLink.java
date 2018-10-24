package rexy.module.wexy.model;

import rexy.config.model.Api;

public class ApiLink {
	private final Api api;
	private final String link;
	
	public ApiLink(Api api, String link) {
		this.api = api;
		this.link = link;
	}
	
	public Api getApi() {
		return api;
	}
	
	public String getLink() {
		return link;
	}

}