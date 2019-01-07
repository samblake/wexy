package rexy.module.wexy.model.input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rexy.module.wexy.WexyServer;

import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

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
		try {
			JsonNode jsonNode = MAPPER.readTree(getValue());
			return MAPPER.writeValueAsString(jsonNode);
		}
		catch (IOException e) {
			logger.warn("Could not parse JSON string: " + getValue(), e);
			return getValue();
		}
	}
	
}