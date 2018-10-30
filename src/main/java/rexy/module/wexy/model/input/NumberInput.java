package rexy.module.wexy.model.input;

public class NumberInput extends Input {
	
	private final String value;
	
	public NumberInput(String label, String name, String value) {
		super("number", label, name);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
