package rexy.module.wexy.actions.endpoint;

import rexy.module.wexy.model.input.Input;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import java.util.List;

public class MockModule extends Module {
	
	private final List<ObjectInstance> presets;
	
	public MockModule(ObjectInstance objectInstance, MBeanInfo mBeanInfo,
			List<Input> attributes, List<ObjectInstance> presets) {
		super(objectInstance, mBeanInfo, attributes);
		this.presets = presets;
	}
	
}