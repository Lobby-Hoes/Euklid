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

import dev.redcodes.euklid.Euklid;
import dev.redcodes.euklid.episode.Episode;

public class Mathefact {

	Episode episode;
	String theme;
	String description;
	String startTime;
	String endTime;
	boolean exists;

	public Mathefact(Episode episode) {

		try {

			this.episode = episode;

			URL url = new URL(Euklid.getMathefactDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject e = iterator.next().getAsJsonObject();

				if (e.get("folgenId").getAsInt() == this.episode.getId()) {

					this.exists = true;

					this.startTime = e.get("startzeit").getAsString();
					this.endTime = e.get("endzeit").getAsString();
					this.theme = e.get("thema").getAsString();
					this.description = e.get("beschreibung").getAsString();

					return;
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public String getTheme() {
		return this.theme;
	}

	public String getDescription() {
		return this.description;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public boolean exists() {
		return this.exists;
	}

	public Episode getEpisode() {
		return this.episode;
	}

}
