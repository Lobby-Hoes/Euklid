package dev.redcodes.euklid.mathefacts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mathefact {

	long id;
	String episodeName;
	String theme;
	String description;
	String startTime;
	String endTime;

	public Mathefact(long id) {
		
		this.id = id;

		try {

			URL url = new URL("https://raw.githubusercontent.com/saphrus/mathefacts/main/data.json");

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();
			
			Iterator<JsonElement> iterator = array.iterator();
			
			while(iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();
				
				if(Integer.valueOf(episode.get("id").getAsString()) == this.id) {
					
					this.episodeName = episode.get("name").getAsString();
					
					return;
				}
			}

		} catch (IOException e) {

		}
	}

}
