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

public class Mathefact {

	String episodeId;
	String episodeName;
	String theme;
	String description;
	String startTime;
	String endTime;
	String spotifyId;
	boolean empty;
	boolean exists;

	public Mathefact(String episodeId) {

		try {

			this.episodeId = episodeId;

			URL url = new URL(Euklid.getDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject episode = iterator.next().getAsJsonObject();

				if (episode.get("folge").getAsString().equals(this.episodeId)) {

					this.exists = true;
					this.episodeName = episode.get("folgenname").getAsString();
					this.spotifyId = episode.get("code").getAsString();

					JsonObject obj = episode.get("mathefacts").getAsJsonObject();
					this.startTime = obj.get("startzeit").getAsString();
					this.endTime = obj.get("endzeit").getAsString();
					this.theme = obj.get("thema").getAsString();
					this.description = obj.get("beschreibung").getAsString();

					if (this.description.equals("")) {
						this.empty = true;
					}

					return;
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public String getEpisodeId() {
		return this.episodeId;
	}

	public String getEpisodeName() {
		return this.episodeName;
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

	public boolean isEmpty() {
		return this.empty;
	}

	public String getSpotifyId() {
		return this.spotifyId;
	}

	public boolean exists() {
		return this.exists;
	}

}