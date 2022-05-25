package dev.redcodes.euklid.mathefacts;

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

public class MathefactUtils {

	public static List<Mathefact> getMathefacts() {

		List<Mathefact> facts = new LinkedList<>();

		try {

			URL url = new URL("https://raw.githubusercontent.com/saphrus/mathefacts/main/data.json");

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				facts.add(new Mathefact(episode.get("folge").getAsString()));
			}

		} catch (IOException e) {

		}

		return facts;
	}

	public static int countMathefacts() {
		try {

			URL url = new URL("https://raw.githubusercontent.com/saphrus/mathefacts/main/data.json");

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			int amount = 0;

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				if (!episode.get("beschreibung").getAsString().equals("")) {
					amount++;
				}
			}

			return amount;

		} catch (IOException e) {

		}

		return 0;
	}

	public static int countEpisodes() {
		try {

			URL url = new URL("https://raw.githubusercontent.com/saphrus/mathefacts/main/data.json");

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			return array.size();

		} catch (IOException e) {

		}

		return 0;
	}

}
