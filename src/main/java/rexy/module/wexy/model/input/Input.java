package rexy.module.wexy.model.input;

public abstract class Input {
	
	private final String type;
	private final String label;
	private final String name;
	
	protected Input(String type, String label, String name) {
		this.type = type;
		this.label = label;
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name;
	}
}
