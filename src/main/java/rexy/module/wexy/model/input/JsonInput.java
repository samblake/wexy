package rexy.module.wexy.model.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rexy.module.wexy.WexyServer;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static rexy.utils.Json.prettyPrint;

public class JsonInput extends ValueInput<String> {
	private static final Logger logger = LogManager.getLogger(WexyServer.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	static {
		MAPPER.enable(INDENT_OUTPUT);
	}
	
	public JsonInput(String label, String name, String value) {
		super("json", label, name, value);
	}
	
	public String getPrettyValue() {
		return isBlank(getValue()) ? getValue() : prettyPrint(getValue());
	}
	
}