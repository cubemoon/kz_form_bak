package com.kuaizhan.form.common;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;


public class JacksonUtils {

    final static ObjectMapper objectMapper;
    static {
        StdSerializerProvider sp = new StdSerializerProvider();
        sp.setNullValueSerializer(new NullSerializer());
        objectMapper = new ObjectMapper(null, sp, null);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static class NullSerializer extends JsonSerializer<Object> {

        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString("");
        }
    }
   
    
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapFromJsonStr(String jsonStr){
		try {
			getObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			getObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			return getObjectMapper().readValue(jsonStr, Map.class);
		} catch (Exception e) {
			throw new RuntimeException("Can not getMapFromJsonStr which jsonStr:"+jsonStr,e);
		}
	}
	
	
	public static String getJsonFromMap(Map<String, ?> map){
		try {
			return getObjectMapper().writeValueAsString(map);
		} catch (Exception e) {
			throw new RuntimeException("Can not getJsonFromMap which map:"+map,e);
		}
	}
	
	public static <T> T toBean(String content, Class<T> valueType)
			throws JsonParseException, JsonMappingException, IOException {
		// allowed json string have unknowed properties
		getObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		getObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(content, valueType);
	}
	

	public static String toJson(Object object) throws IOException {
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, object);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
		return null;
	}
}
