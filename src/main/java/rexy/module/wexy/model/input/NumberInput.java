package rexy.module.wexy.model.input;

public class NumberInput extends ValueInput<Long> {
	
	public NumberInput(String label, String name, Long value) {
		super("number", label, name, value);
	}
	
}