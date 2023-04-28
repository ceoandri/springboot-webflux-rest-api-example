package gratis.contoh.api.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonDateUtil implements JsonSerializer<Date>, JsonDeserializer<Date> {
	
	SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy");
	
	@Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String dString = jsonElement.getAsString();
        java.util.Date temp = null;
		try {
			temp = df.parse(dString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(temp.getTime());
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(df.format(date));
    }
}
