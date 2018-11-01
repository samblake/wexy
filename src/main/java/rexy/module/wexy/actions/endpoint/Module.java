package rexy.module.wexy.actions.endpoint;

import rexy.module.wexy.model.input.Input;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import java.util.List;

public class Module {
	
	private final ObjectInstance objectInstance;
	private final MBeanInfo mBeanInfo;
	private final List<Input> attributes;
	
	public Module(ObjectInstance objectInstance, MBeanInfo mBeanInfo, List<Input> attributes) {
		this.objectInstance = objectInstance;
		this.mBeanInfo = mBeanInfo;
		this.attributes = attributes;
	}
	
	public List<Input> getInputs() {
		return attributes;
	}
	
}