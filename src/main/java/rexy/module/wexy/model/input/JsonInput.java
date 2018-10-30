package rexy.module.wexy.model.input;

public class JsonInput extends Input {
	
	private final String value;
	
	public JsonInput(String label, String name, String value) {
		super("json", label, name);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
