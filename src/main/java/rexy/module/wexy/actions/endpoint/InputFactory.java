package rexy.module.wexy.actions.endpoint;

import rexy.module.wexy.model.input.CheckboxInput;
import rexy.module.wexy.model.input.Input;
import rexy.module.wexy.model.input.JsonInput;
import rexy.module.wexy.model.input.TextInput;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectInstance;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Character.toUpperCase;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

public class InputFactory {
	
	public static Input createInput(ObjectInstance objectInstance, MBeanAttributeInfo attributeInfo, Object value) {
		String beanName = objectInstance.getObjectName().getKeyProperty("name");
		String attributeName = attributeInfo.getName();
		
		String label = join(" ", splitByCharacterTypeCamelCase(attributeName));
		label = toUpperCase(label.charAt(0)) + label.substring(1);
		
		Input input = createInput(attributeInfo, beanName, attributeName, label, value);
		
		if (!attributeInfo.isWritable()) {
			input.setDisabled();
		}
		
		return input;
	}
	
	private static Input createInput(MBeanAttributeInfo attributeInfo,
			String beanName, String attributeName, String label, Object value) {
		
		if (beanName.equals("mock") && attributeName.equals("Body")) {
			return new JsonInput(label, attributeName, value.toString());
		}
		
		String type = attributeInfo.getType();
		if (type.equals("boolean")) {
			return new CheckboxInput(label, attributeName, parseBoolean(value.toString()));
		}
		
		return new TextInput(label, attributeName, value.toString());
	}
	
}