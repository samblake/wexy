package rexy.module.wexy.actions.endpoint;

import rexy.module.wexy.model.input.Input;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Module {
	
	private final ObjectInstance objectInstance;
	private final MBeanInfo mBeanInfo;
	
	public Module(ObjectInstance objectInstance, MBeanInfo mBeanInfo) {
		this.objectInstance = objectInstance;
		this.mBeanInfo = mBeanInfo;
	}
	
	public List<Input> getInputs() {
		return stream(mBeanInfo.getAttributes()).map(mbi -> InputFactory.createInput(objectInstance, mbi)).collect(toList());
	}
	
}