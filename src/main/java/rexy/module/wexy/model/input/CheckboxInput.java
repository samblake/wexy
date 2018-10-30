package rexy.module.wexy.model.input;

public class CheckboxInput extends Input {
	
	private final boolean value;
	
	public CheckboxInput(String label, String name, boolean value) {
		super("checkbox", label, name);
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
}