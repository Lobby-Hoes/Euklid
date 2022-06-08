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

import dev.redcodes.euklid.Euklid;
import dev.redcodes.euklid.episode.Episode;

public class MathefactUtils {

	public static List<Mathefact> getMathefacts() {

		List<Mathefact> facts = new LinkedList<>();

		try {

			URL url = new URL(Euklid.getMathefactDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				Mathefact fact = new Mathefact(new Episode(episode.get("folgenId").getAsInt()));
				if (fact.exists()) {
					facts.add(fact);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return facts;
	}

	public static int countMathefacts() {
		try {

			URL url = new URL(Euklid.getMathefactDataUrl());

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
