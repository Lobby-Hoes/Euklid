package dev.redcodes.euklid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.redcodes.euklid.general.info.InfoCommand;
import dev.redcodes.euklid.mathefacts.commands.MathefactAutoComplete;
import dev.redcodes.euklid.mathefacts.commands.MathefactCommand;
import dev.redcodes.euklid.stadtgeschichten.commands.StadtGeschichteAutoComplete;
import dev.redcodes.euklid.stadtgeschichten.commands.StadtGeschichteCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Euklid {

    private static JDA jda;

    private static Logger logger = LoggerFactory.getLogger(Euklid.class);

    private static String version = "Alpha 1.3";

    private static boolean dev = true;

    private static String year;

    private static String icon = "https://i.imgur.com/xARAVsM.png";

    private static Instant online = Instant.now();

    private static String episodeData = "https://raw.githubusercontent.com/Lobby-Hoes/hobbylos-data/main/folgen.json";
    private static String mathefactData = "https://raw.githubusercontent.com/Lobby-Hoes/hobbylos-data/main/mathefacts.json";
    private static String storyData = "https://raw.githubusercontent.com/Lobby-Hoes/hobbylos-data/main/staedtegeschichten.json";
    private static String playlistData = "https://raw.githubusercontent.com/Lobby-Hoes/hobbylos-data/main/playlist.json";

    public static void main(String[] args) {

        String token = args[0];

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setActivity(Activity.watching("Bot starting..."));
        builder.setStatus(OnlineStatus.IDLE);

        List<GatewayIntent> intents = new ArrayList<>();
        intents.addAll(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        intents.remove(GatewayIntent.GUILD_PRESENCES);

        builder.setEnabledIntents(intents);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.addEventListeners(new MathefactAutoComplete());
        builder.addEventListeners(new MathefactCommand());
        builder.addEventListeners(new InfoCommand());
        builder.addEventListeners(new StadtGeschichteAutoComplete());
        builder.addEventListeners(new StadtGeschichteCommand());

        jda = builder.build();

        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        logger.info("The bot is now online!");

        shutdown();
        runLoop();

    }

    private static boolean shutdown = false;

    private static void shutdown() {

        new Thread(() -> {

            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("stop")) {
                        shutdown = true;
                        if (jda != null) {
                            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                            jda.shutdown();
                            logger.info("The bot is now offline!");
                        }
                        reader.close();
                        System.exit(0);
                        break;

                    } else {
                        logger.warn("Unknown Command \"" + line + "\"");
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }

    private static void runLoop() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!shutdown) {
                    onSecond();
                }
            }
        }, 2000, (15 * 1000));
    }

    private static boolean commandCheck = true;
    private static String[] status = new String[]{"mit euklid", "%members% Users", "%version%"};
    private static Random rand = new Random();

    private static void onSecond() {

        if (commandCheck) {
            commandCheck = false;

            List<CommandData> cmds = new ArrayList<>();

            cmds.add(Commands.slash("mathefact", "Suche nach einem Mathefact aus dem Hobbylos-Podcast.")
                    .addOptions(new OptionData(OptionType.STRING, "suche",
                            "Suche nach der Folgennummer, Folgenname oder dem Mathefactthema.", true)
                            .setAutoComplete(true)));
            cmds.add(Commands.slash("stadtgeschichte", "Suche nach einer Stadtgeschichte aus dem Hobbylos-Podcast.")
                    .addOptions(new OptionData(OptionType.STRING, "suche",
                            "Suche nach der Folgennummer, Folgenname oder dem Titel der Stadtgeschichte.", true)
                            .setAutoComplete(true)));
            cmds.add(Commands.slash("playlist", "Suche nach einem Playlist-Eintrag aus der Lobbyhoes Playlist.").addOptions(new OptionData(OptionType.STRING, "suche", "Suche nach dem Songtitel, der Folgennummer oder dem Namen der Folge", true).setAutoComplete(true));
            cmds.add(Commands.slash("info", "Information über den Bot."));

            if (dev) {
                jda.getGuildById(580732235313971211L).updateCommands().addCommands(cmds)
                        .queue(commands -> logger.info("Commands published"));
                jda.updateCommands().addCommands(new ArrayList<>()).queue();
            } else {
                jda.updateCommands().addCommands(cmds).queue(commands -> logger.info("Commands published"));
                jda.getGuildById(580732235313971211L).updateCommands().addCommands(new ArrayList<>()).queue();
            }

        }

        int i = rand.nextInt(status.length);

        int users = 0;

        for (Guild guild : jda.getGuilds()) {
            users = users + guild.getMemberCount();
        }

        String text = status[i].replace("%members%", String.valueOf(users)).replace("%version%", version)
                .replace("%guilds%", String.valueOf(jda.getGuilds().size()));

        if (!jda.getPresence().getActivity().getName().equals(text)) {

            if (!dev) {
                if (text.contains("axolotl")) {
                    jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening(text));
                } else if (text.contains("User") || text.contains("Guilds") || text.contains("axolotl")) {
                    jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching(text));
                } else {
                    jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing(text));
                }
            } else {
                jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching("Development"));
            }
        } else {
            onSecond();
        }

    }

    public static String getYear() {
        return year;
    }

    public static String getIconUrl() {
        return icon;
    }

    public static Instant getOnlineSince() {
        return online;
    }

    public static String getVersion() {
        return version;
    }

    public static String getEpisodeDataUrl() {
        return episodeData;
    }

    public static String getMathefactDataUrl() {
        return mathefactData;
    }

    public static String getStoryDataUrl() {
        return storyData;
    }

    public static String getPlaylistDataUrl() {
        return playlistData;
    }

}
