package dev.redcodes.euklid.stadtgeschichten;

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

public class StadtGeschichte {

	String title;
	String location;
	String story;
	StadtGeschichtenTyp type;
	double lat;
	double lon;
	Episode episode;
	boolean exists;
	String startTime;
	String endTime;

	public StadtGeschichte(Episode episode, String title) {
		this.episode = episode;
		this.title = title;

		try {

			URL url = new URL(Euklid.getStoryDataUrl());

			URLConnection connection = url.openConnection();

			connection.connect();

			JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
			JsonObject rootObj = element.getAsJsonObject();
			JsonArray array = rootObj.get("data").getAsJsonArray();

			Iterator<JsonElement> iterator = array.iterator();

			while (iterator.hasNext()) {
				JsonObject ep = iterator.next().getAsJsonObject();

				if (ep.get("folgenId").getAsInt() == this.episode.getId()) {

					this.exists = true;

					this.startTime = ep.get("startzeit").getAsString();
					this.endTime = ep.get("endzeit").getAsString();

					Iterator<JsonElement> storyIterator = ep.get("geschichten").getAsJsonArray().iterator();

					while (storyIterator.hasNext()) {
						JsonObject storyObj = storyIterator.next().getAsJsonObject();

						if (storyObj.get("titel").getAsString().equals(this.title)) {
							this.location = storyObj.get("ort").getAsString();
							this.story = storyObj.get("geschichte").getAsString();
							this.type = StadtGeschichtenTyp.valueOf(storyObj.get("typ").getAsString().toUpperCase());
							this.lat = storyObj.get("geo").getAsJsonArray().get(0).getAsDouble();
							this.lon = storyObj.get("geo").getAsJsonArray().get(1).getAsDouble();
						}
					}
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public String getStory() {
		return story;
	}

	public StadtGeschichtenTyp getType() {
		return type;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public Episode getEpisode() {
		return episode;
	}

	public boolean exists() {
		return exists;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

}
