package co.com.soaint.foundation.canonical.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String formatted = sdf.format(date);
            jsonGenerator.writeString(formatted);
        }
    }
}
