package dev.redcodes.euklid.general.info;

import java.time.Instant;

import dev.redcodes.euklid.Euklid;
import dev.redcodes.euklid.episode.EpisodeUtils;
import dev.redcodes.euklid.mathefacts.MathefactUtils;
import dev.redcodes.euklid.stadtgeschichten.StadtGeschichtenUtils;
import dev.redcodes.euklid.util.MessageColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InfoCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {

		String[] args = e.getCommandPath().split("/");

		if (args[0].equalsIgnoreCase("info")) {
			e.deferReply().queue();

			int users = 0;

			for (Guild guild : e.getJDA().getGuilds()) {
				users += guild.getMemberCount();
			}

			long duration = Instant.now().getEpochSecond() - Euklid.getOnlineSince().getEpochSecond();
			long days = duration / 86400;
			long hours = duration / 3600 % 24;
			long minutes = duration / 60 % 60;
			long seconds = duration / 1 % 60;

			EmbedBuilder msg = new EmbedBuilder();
			msg.setTitle("Euklid");
			msg.setDescription(
					"Ein Discord-Bot basierend auf dem Spotify Original Podcast \"**Hobbylos**\" von Julien Bam und Rezo. Entwickelt von [Redi](https://redcodes.dev/) und den [Nerdy Lobbyhoes](https://github.com/Lobby-Hoes).");
			msg.addField("Discord-Server", String.valueOf(e.getJDA().getGuilds().size()), true);
			msg.addField("Users", String.valueOf(users), true);
			msg.addField("Uptime",
					days + " Tage, " + hours + " Stunden, " + minutes + " Minuten, " + seconds + " Sekunden", true);
			msg.addField("Folgen", String.valueOf(EpisodeUtils.countEpisodes()), true);
			msg.addField("Mathefacts", String.valueOf(MathefactUtils.countMathefacts()), true);
			msg.addField("Stadtgeschichten", String.valueOf(StadtGeschichtenUtils.countStadtGeschichten()), true);
			msg.addField("Bot-Version", Euklid.getVersion(), true);
			msg.addBlankField(true);
			msg.addBlankField(true);
			msg.addBlankField(true);
			msg.addField("**Hobbylos**", """


					• 🎙 **[Podcast](https://open.spotify.com/show/6UUIXmp1V0fK4ZpK7vzAbQ)**
					• 📡 **[Subreddit](https://www.reddit.com/r/Hobbylos/)**
					• 📸 **[Rezo](https://www.instagram.com/rezo/)** & **[Ju](https://www.instagram.com/julienbam/)**
					""", true);
			msg.setThumbnail(Euklid.getIconUrl());
			msg.setImage("https://i.imgur.com/TtfcXg9.png");
			msg.setColor(MessageColor.BACKGROUND.getColor());
			msg.setFooter("© Euklid Bot " + Euklid.getYear(), Euklid.getIconUrl());
			e.getHook().editOriginalEmbeds(msg.build()).queue();
		}

	}

}
