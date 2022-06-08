package dev.redcodes.euklid.stadtgeschichten.commands;

import java.util.LinkedList;
import java.util.List;

import dev.redcodes.euklid.Euklid;
import dev.redcodes.euklid.episode.Episode;
import dev.redcodes.euklid.stadtgeschichten.StadtGeschichte;
import dev.redcodes.euklid.util.MessageColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class StadtGeschichteCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {

		String[] args = e.getCommandPath().split("/");

		if (args[0].equalsIgnoreCase("stadtgeschichte")) {
			e.deferReply().complete();

			String[] storyData = e.getOption("suche").getAsString().split("_");

			if (storyData.length == 2) {

				Episode ep = new Episode(Integer.parseInt(storyData[0]));
				
				StadtGeschichte story = new StadtGeschichte(ep, storyData[1]);

				if (story.exists()) {

					EmbedBuilder msg = new EmbedBuilder();
					msg.setTitle("Stadtgeschichte");
					msg.setDescription(story.getStory());
					msg.addField("Titel", story.getTitle(), true);
					msg.addField("Ort", story.getLocation() + "\n[" + story.getLat() + ", " + story.getLon()
							+ "](https://www.google.com/maps/search/" + story.getLat() + ",+" + story.getLon() + ")",
							true);
					msg.addField("Typ", story.getType().toString(), true);
					msg.addField("Episodenzeitpunkt", "`" + story.getStartTime() + "` - `" + story.getEndTime() + "`",
							true);
					msg.addField("Episode", "Folge " + ep.getId() + "\n> `" + ep.getName() + "`",
							true);
					msg.addField("Spotify-ID", "`" + ep.getCode() + "`", true);
					msg.setThumbnail("https://i.imgur.com/rqizpAj.png");
					msg.setColor(MessageColor.SUCCESS.getColor());
					msg.setFooter("© Euklid Bot " + Euklid.getYear(), Euklid.getIconUrl());

					List<Button> buttons = new LinkedList<>();
					buttons.add(Button.link("https://open.spotify.com/episode/" + ep.getCode(), "Zur Folge")
							.withEmoji(Emoji.fromEmote(e.getJDA().getEmoteById(979039391961989150L))));

					e.getHook().editOriginalEmbeds(msg.build()).setActionRow(buttons).queue();
					return;
				}
			}
			EmbedBuilder msg = new EmbedBuilder();
			msg.setTitle("Ungültige Stadtgeschichte!");
			msg.setColor(MessageColor.FAILED.getColor());
			msg.setFooter("© Euklid Bot " + Euklid.getYear(), Euklid.getIconUrl());
			e.getHook().editOriginalEmbeds(msg.build()).queue();
		}

	}

}
