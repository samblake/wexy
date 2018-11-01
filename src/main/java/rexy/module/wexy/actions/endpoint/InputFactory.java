package rexy.module.wexy.actions.endpoint;

import rexy.module.wexy.model.input.CheckboxInput;
import rexy.module.wexy.model.input.Input;
import rexy.module.wexy.model.input.JsonInput;
import rexy.module.wexy.model.input.TextInput;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectInstance;

import static java.lang.Character.toUpperCase;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

public class InputFactory {
	
	public static Input createInput(ObjectInstance objectInstance, MBeanAttributeInfo mBeanInfo) {
		String beanName = objectInstance.getObjectName().getKeyProperty("name");
		String attributeName = mBeanInfo.getName();
		
		String label = join(" ", splitByCharacterTypeCamelCase(attributeName));
		label = toUpperCase(label.charAt(0)) + label.substring(1);
		
		Input input = createInput(mBeanInfo, beanName, attributeName, label);
		
		if (!mBeanInfo.isWritable()) {
			input.setDisabled();
		}
		
		return input;
	}
	
	private static Input createInput(MBeanAttributeInfo mBeanInfo, String beanName, String attributeName, String label) {
		if (beanName.equals("mock") && attributeName.equals("Body")) {
			return new JsonInput(label, attributeName, "");
		}
		
		String type = mBeanInfo.getType();
		if (type.equals("boolean")) {
			return new CheckboxInput(label, attributeName, false);
		}
		
		return new TextInput(label, attributeName, "");
	}
	
}