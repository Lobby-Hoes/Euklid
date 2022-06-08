package dev.redcodes.euklid.episode;

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

import dev.redcodes.euklid.Euklid;

public class Episode {

	int id;
	String name;
	String code;

	public Episode(int id) {
		this.id = id;

		try {

			URL url = new URL(Euklid.getEpisodeDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				if (episode.get("folgenId").getAsInt() == this.id) {
					this.name = episode.get("folgenname").getAsString();
					this.code = episode.get("code").getAsString();
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	

}
