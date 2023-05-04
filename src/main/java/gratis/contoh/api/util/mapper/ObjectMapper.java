package gratis.contoh.api.util.mapper;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectMapper<T, Z> {
	private Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new GsonDateAdapter())
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
		    .create();

	public Gson getGson() {
		return this.gson;
	}
	
	public T convert(Class<T> classOfTarget, 
			Z source, 
			FieldNamingPolicy... namingPolicy) {
		reformGson(namingPolicy);
		
		T res = gson.fromJson(gson.toJson(source), classOfTarget);
		return res;
	}
	
	public List<T> convert(Class<T> classOfTarget, 
			List<Z> source, 
			FieldNamingPolicy... namingPolicy) {
		reformGson(namingPolicy);
		
		List<T> res = new ArrayList<T>();
		
		for (int i = 0; i < source.size(); i++) {			
			res.add(gson.fromJson(gson.toJson(source.get(i)), classOfTarget));
		}
		
		return res;
	}
	
	public T convert(Class<T> classOfTarget, 
			String source, 
			FieldNamingPolicy... namingPolicy) {
		reformGson(namingPolicy);
		T res = gson.fromJson(source, classOfTarget);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> convertToList(Class<T> classOfTarget, 
			Object source, 
			FieldNamingPolicy... namingPolicy) {
		List<T> res = new ArrayList<T>();
		
		reformGson(namingPolicy);
		
		List<Object> sourceList = gson.fromJson(gson.toJson(source), List.class);
		
		for (int i = 0; i < sourceList.size(); i++) {	
			res.add(gson.fromJson(gson.toJson(sourceList.get(i)), classOfTarget));
		}
		
		return res;
	}
	
	private void reformGson(FieldNamingPolicy... namingPolicy) {
		if (namingPolicy.length > 0) {
			gson = new GsonBuilder()
					.registerTypeAdapter(Date.class, new GsonDateAdapter())
					.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
				    .setFieldNamingPolicy(namingPolicy[0])
				    .create();
		}
	}
	
}
