package rexy.module.wexy.model.input;

public class TextInput extends Input {
	
	private final String value;
	
	public TextInput(String label, String name, String value) {
		super("text", label, name);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
