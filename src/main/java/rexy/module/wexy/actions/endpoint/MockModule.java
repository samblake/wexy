package rexy.module.wexy.actions.endpoint;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import java.util.List;

public class MockModule extends Module {
	
	private final List<ObjectInstance> presets;
	
	public MockModule(ObjectInstance objectInstance, MBeanInfo mBeanInfo, List<ObjectInstance> presets) {
		super(objectInstance, mBeanInfo);
		this.presets = presets;
	}
	
}