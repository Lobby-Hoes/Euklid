package dev.redcodes.euklid.mathefacts.commands;

import java.util.LinkedList;
import java.util.List;

import dev.redcodes.euklid.Euklid;
import dev.redcodes.euklid.episode.Episode;
import dev.redcodes.euklid.mathefacts.Mathefact;
import dev.redcodes.euklid.util.MessageColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MathefactCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {

		String[] args = e.getCommandPath().split("/");

		if (args[0].equalsIgnoreCase("mathefact")) {
			e.deferReply().complete();

			try {
				Episode ep = new Episode(e.getOption("suche").getAsInt());

				Mathefact fact = new Mathefact(ep);

				if (fact.exists()) {
					EmbedBuilder msg = new EmbedBuilder();
					msg.setTitle("Mathefact");
					msg.addField("Faktthema", fact.getTheme(), true);
					msg.addField("Faktbeschreibung", fact.getDescription(), true);
					msg.addField("Episodenzeitpunkt", "`" + fact.getStartTime() + "` - `" + fact.getEndTime() + "`",
							true);
					msg.addField("Episode", "Folge " + ep.getId() + "\n> `" + ep.getName() + "`", true);
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

			} catch (NumberFormatException ex) {

			}
			EmbedBuilder msg = new EmbedBuilder();
			msg.setTitle("Ungültiger Mathefact!");
			msg.setColor(MessageColor.FAILED.getColor());
			msg.setFooter("© Euklid Bot " + Euklid.getYear(), Euklid.getIconUrl());
			e.getHook().editOriginalEmbeds(msg.build()).queue();

		}

	}

}
