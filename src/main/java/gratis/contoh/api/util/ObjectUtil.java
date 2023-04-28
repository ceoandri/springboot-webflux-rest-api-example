package gratis.contoh.api.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectUtil<T, Z> {
	private Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new GsonDateUtil())
		    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		    .create();
	
	public T convert(Class<T> classOfTarget, Z source) {
		T res = gson.fromJson(gson.toJson(source), classOfTarget);
		return res;
	}
	
	public List<T> convert(Class<T> classOfTarget, List<Z> source) {
		List<T> res = new ArrayList<T>();
		
		for (int i = 0; i < source.size(); i++) {			
			res.add(gson.fromJson(gson.toJson(source.get(i)), classOfTarget));
		}
		
		return res;
	}
	
}
