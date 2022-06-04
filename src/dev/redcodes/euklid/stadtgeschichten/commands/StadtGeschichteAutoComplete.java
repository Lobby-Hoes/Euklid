package dev.redcodes.euklid.stadtgeschichten.commands;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import dev.redcodes.euklid.mathefacts.Mathefact;
import dev.redcodes.euklid.stadtgeschichten.StadtGeschichte;
import dev.redcodes.euklid.stadtgeschichten.StadtGeschichtenUtils;
import info.debatty.java.stringsimilarity.NGram;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;

public class StadtGeschichteAutoComplete extends ListenerAdapter {

	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent e) {
		String[] args = e.getCommandPath().split("/");

		if (args[0].equalsIgnoreCase("stadtgeschichte")) {

			String input = e.getFocusedOption().getValue();

			NGram ng = new NGram();

			MultiValuedMap<StadtGeschichte, Double> rawMatches = new ArrayListValuedHashMap<>();

			for (StadtGeschichte story : StadtGeschichtenUtils.getStadtGeschichten()) {
				double episodeDistance = ng.distance(input, story.getTitle());
				double themeDistance = ng.distance(input, new Mathefact(story.getEpisode()).getEpisodeName());

				rawMatches.put(story, episodeDistance);
				rawMatches.put(story, themeDistance);

				if (input.equals(story.getEpisode())) {
					rawMatches.put(story, 0.1d);
				}
			}

			Map<StadtGeschichte, Double> matches = new LinkedHashMap<>();

			rawMatches.entries().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> {

				if (x.getValue() > 0.0d) {
					if (matches.containsKey(x.getKey())) {
						if (matches.get(x.getKey()) > x.getValue()) {
							matches.put(x.getKey(), x.getValue());
						}
					} else {
						matches.put(x.getKey(), x.getValue());
					}
				}
			});

			List<Choice> choices = new LinkedList<>();

			for (Entry<StadtGeschichte, Double> entry : matches.entrySet()) {
				if (choices.size() < 25) {
					Choice choice = new Choice(
							"Folge " + entry.getKey().getEpisode() + " - " + entry.getKey().getTitle(),
							entry.getKey().getEpisode() + "_" + entry.getKey().getTitle());
					choices.add(choice);
				}
			}

			e.replyChoices(choices).queue();
		}
	}

}
