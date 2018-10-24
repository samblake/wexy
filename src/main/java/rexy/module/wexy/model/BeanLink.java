package rexy.module.wexy.model;

public class BeanLink {
	private final String bean;
	private final String link;
	
	public BeanLink(String bean, String link) {
		this.bean = bean;
		this.link = link;
	}
	
	public String getBean() {
		return bean;
	}
	
	public String getLink() {
		return link;
	}
	
}