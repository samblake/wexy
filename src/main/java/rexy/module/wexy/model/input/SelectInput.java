package rexy.module.wexy.model.input;

// TODO for enums
public class SelectInput extends Input {
	
	private final String value;
	
	public SelectInput(String label, String name, String value) {
		super("select", label, name);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public String[] getOptions() {
		return new String[] {};
	}

}