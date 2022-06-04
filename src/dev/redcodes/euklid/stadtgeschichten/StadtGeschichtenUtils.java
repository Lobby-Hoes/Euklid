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

public class StadtGeschichtenUtils {

	public static List<StadtGeschichte> getStadtGeschichten() {

		List<StadtGeschichte> storys = new LinkedList<>();

		try {

			URL url = new URL(Euklid.getDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				if (episode.has("staedtegeschichten")) {
					JsonObject geschichten = episode.get("staedtegeschichten").getAsJsonObject();

					Iterator<JsonElement> storyIterator = geschichten.get("geschichten").getAsJsonArray().iterator();

					while (storyIterator.hasNext()) {
						JsonObject storyObj = storyIterator.next().getAsJsonObject();

						storys.add(new StadtGeschichte(episode.get("folge").getAsString(),
								storyObj.get("titel").getAsString()));

					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return storys;
	}

}
