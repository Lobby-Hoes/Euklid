package dev.redcodes.euklid.episode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.redcodes.euklid.Euklid;

public class EpisodeUtils {

	public static List<Episode> getEpisodes() {

		LinkedList<Episode> list = new LinkedList<>();

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

				list.add(new Episode(episode.get("folgenId").getAsInt()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static int countEpisodes() {
		try {

			URL url = new URL(Euklid.getEpisodeDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			return array.size();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
