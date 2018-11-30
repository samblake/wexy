package rexy.module.wexy.mbean;

import rexy.config.model.Api;
import rexy.config.model.Endpoint;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Short.parseShort;

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
	
	public void updateValues(Endpoint endpoint, String moduleName, Map<String, String> params) throws JMException {
		ObjectName objectName = new RexyQueryBuilder()
				.withApi(endpoint.getApi().getName())
				.withEndpoint(endpoint.getName())
				.withName(moduleName)
				.build();
		
		MBeanInfo info = server.getMBeanInfo(objectName);
		AttributeList attributes = new AttributeList(info.getAttributes().length);
		for (MBeanAttributeInfo attributeInfo : info.getAttributes()) {
			String param = params.get(attributeInfo.getName()).trim();
			Object value = convert(param, attributeInfo.getType());
			attributes.add(new Attribute(attributeInfo.getName(), value));
		}
		
		server.setAttributes(objectName, attributes);
	}
	
	private Object convert(String value, String type) {
		switch (type) {
			case "java.lang.String": return value;
			case "int": return parseInt(value);
			case "short": return parseShort(value);
			case "long": return parseLong(value);
			case "float": return parseFloat(value);
			case "double": return parseDouble(value);
			case "boolean": return value != null;
			case "char": return value.charAt(0);
			case "java.util.Map": return parseMap(value);
			
			default: throw new RuntimeException("No handler for type: " + type);
		}
	}
	
	// FIXME encode maps in a better way than this
	private Map<Object, Object> parseMap(String value) {
		Map<Object, Object> map = new HashMap<>();
		if (value != null && !value.isEmpty()) {
			String[] entries = value.substring(1, value.length() - 1).split(",");
			for (String entry : entries) {
				String[] pair = entry.trim().split("=");
				map.put(pair[0], pair[1]);
			}
		}
		return map;
	}
}