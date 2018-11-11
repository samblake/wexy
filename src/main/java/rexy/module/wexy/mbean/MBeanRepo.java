package rexy.module.wexy.mbean;

import rexy.config.model.Api;
import rexy.config.model.Endpoint;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class MBeanRepo {
	
	private final MBeanServer server;
	
	public MBeanRepo() {
		this.server = ManagementFactory.getPlatformMBeanServer();
	}
	
	public Set<ObjectInstance> search(MBeanQueryBuilder queryBuilder) {
		return server.queryMBeans(queryBuilder.build(), null);
	}
	
	public MBeanInfo getInfo(ObjectInstance objectInstance) {
		try {
			return server.getMBeanInfo(objectInstance.getObjectName());
		}
		catch (JMException e) {
			throw new RuntimeException("Could not get MBean info", e);
		}
	}
	
	public Object getAttributeValue(ObjectInstance oi, MBeanAttributeInfo mbai) {
		try {
			return server.getAttribute(oi.getObjectName(), mbai.getName());
		}
		catch (JMException e) {
			throw new RuntimeException("Could not get attriute value", e);
		}
	}
	
	public Set<ObjectInstance> findForEndpoint(Api api, Endpoint endpoint) {
		MBeanQueryBuilder query = new RexyQueryBuilder().withApi(api.getName()).withEndpoint(endpoint.getName());
		return search(query);
	}
	
	public void applyPreset(Endpoint endpoint, String presetName) throws JMException {
		MBeanQueryBuilder query = new RexyQueryBuilder()
				.withApi(endpoint.getApi().getName())
				.withEndpoint(endpoint.getName())
				.withComponent("preset")
				.withName(presetName);
		
		server.invoke(query.build(), "set", null, null);
	}
}