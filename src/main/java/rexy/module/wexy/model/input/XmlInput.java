package rexy.module.wexy.model.input;

import rexy.utils.Xml;

public class XmlInput extends FormattedInput {
	
	public XmlInput(String label, String name, String value) {
		super("xml", label, name, value);
	}
	
	@Override
	protected String prettyPrint(String value) {
		return Xml.prettyPrint(value);
	}
	
}