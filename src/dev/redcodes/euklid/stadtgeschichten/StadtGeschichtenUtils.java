package dev.redcodes.euklid.stadtgeschichten;

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
import dev.redcodes.euklid.episode.Episode;

public class StadtGeschichtenUtils {

	public static List<StadtGeschichte> getStadtGeschichten() {

		List<StadtGeschichte> storys = new LinkedList<>();

		try {

			URL url = new URL(Euklid.getStoryDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject e = iterator.next().getAsJsonObject();

				Iterator<JsonElement> storyIterator = e.get("geschichten").getAsJsonArray().iterator();

				while (storyIterator.hasNext()) {
					JsonObject storyObj = storyIterator.next().getAsJsonObject();

					storys.add(new StadtGeschichte(new Episode(e.get("folgenId").getAsInt()),
							storyObj.get("titel").getAsString()));

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return storys;
	}

	public static int countStadtGeschichten() {

		int count = 0;
		
		try {

			URL url = new URL(Euklid.getStoryDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject e = iterator.next().getAsJsonObject();

				

				count += e.get("geschichten").getAsJsonArray().size();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return count;
	}

}
