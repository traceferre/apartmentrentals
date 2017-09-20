package com.libertymutual.goforcode.spark.app.utilities;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonHelper {
	
	 public static Map toMap(String json) {
	   ObjectMapper mapper = new ObjectMapper();
	   try {
	       return mapper.readValue(json, Map.class);
	   } catch (IOException e) { throw new RuntimeException(e); }
	 }
	 
	 public static Map[] toMaps(String json) {
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	        return mapper.readValue(json, Map[].class);
	    } catch (IOException e) { throw new RuntimeException(e); } }
}

