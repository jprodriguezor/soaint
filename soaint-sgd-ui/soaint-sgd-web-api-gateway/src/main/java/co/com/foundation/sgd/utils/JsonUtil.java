package co.com.foundation.sgd.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * Created by diony on 08/02/2018.
 */
public class JSONUtil {

    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object  fromJson(String json, Class _class) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,_class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
