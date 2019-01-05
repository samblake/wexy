package rexy.module.wexy.actions.endpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rexy.module.wexy.model.input.CheckboxInput;
import rexy.module.wexy.model.input.Input;
import rexy.module.wexy.model.input.JsonInput;
import rexy.module.wexy.model.input.TagInput;
import rexy.module.wexy.model.input.TextInput;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectInstance;
import java.util.Collection;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Character.toUpperCase;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

public class InputFactory {
	private static final Logger logger = LogManager.getLogger(InputFactory.class);
	
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
		
		switch (type) {
			case "boolean":
				return new CheckboxInput(label, attributeName, parseBoolean(value.toString()));
				
			case "int":
			case "long":
			case "short":
			case "float":
			case "double":
			case "btye":
			case "char":
				return new TextInput(label, attributeName, value.toString());
		}
		
		if (value instanceof String) {
			return new TextInput(label, attributeName, value.toString());
		}
		
		if (value instanceof Collection) {
			return new TagInput(label, attributeName, value.toString());
		}

		if (value instanceof Map) {
			String stringValue = ((Map<?, ?>) value).entrySet().stream()
					.map(e -> e.getKey() + ": " + e.getValue())
					.collect(joining(","));
			return new TagInput(label, attributeName, stringValue);
		}
		
		throw new RuntimeException("Unknown type: " + type);
	}
	
}