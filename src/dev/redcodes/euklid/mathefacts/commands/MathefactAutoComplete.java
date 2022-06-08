package dev.redcodes.euklid.mathefacts.commands;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import dev.redcodes.euklid.mathefacts.Mathefact;
import dev.redcodes.euklid.mathefacts.MathefactUtils;
import info.debatty.java.stringsimilarity.NGram;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;

public class MathefactAutoComplete extends ListenerAdapter {

	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent e) {

		String[] args = e.getCommandPath().split("/");

		if (args[0].equalsIgnoreCase("mathefact")) {

			String input = e.getFocusedOption().getValue();

			NGram ng = new NGram();

			MultiValuedMap<Mathefact, Double> rawMatches = new ArrayListValuedHashMap<>();

			for (Mathefact fact : MathefactUtils.getMathefacts()) {
				if (fact.exists()) {

					double episodeDistance = ng.distance(input, fact.getEpisode().getName());
					double themeDistance = ng.distance(input, fact.getTheme());

					rawMatches.put(fact, episodeDistance);
					rawMatches.put(fact, themeDistance);

					if (input.equals(String.valueOf(fact.getEpisode().getId()))) {
						rawMatches.put(fact, 0.1d);
					}

				}
			}

			Map<Mathefact, Double> matches = new LinkedHashMap<>();

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

			for (Entry<Mathefact, Double> entry : matches.entrySet()) {
				if (choices.size() < 25) {
					Choice choice = new Choice(
							"Folge " + entry.getKey().getEpisode().getId() + " - " + entry.getKey().getTheme(),
							entry.getKey().getEpisode().getId());
					choices.add(choice);
				}
			}

			e.replyChoices(choices).queue();
		}
	}
}
