package ru.cornivella.discord.tools;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import ru.cornivella.discord.utils.Configuration;

/**
 * @author Sh18RW
 */
public class Bot {
    private static volatile Bot instance;

    public static Bot getInstance() {
        Bot localInstance = instance;

        if (localInstance == null) {
            synchronized (Bot.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Bot();
                }
            }
        }

        return localInstance;
    }

    private BotState botState;

    private Logger logger;
    private JDABuilder builder;

    private JDA jdaBot;

    public static void startBot() throws InterruptedException {
        Bot instance = getInstance();

        instance.botState = BotState.Work;
        instance.jdaBot = instance.builder
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.customStatus(Configuration.getProperty("bot.activity")))
            .build().awaitReady();

        for (Guild g : instance.jdaBot.getGuilds()) {
            instance.logger.info(g.getName());
        }
    }

    private Bot() {
        botState = BotState.Setup;

        logger = LogManager.getLogger(Bot.class);
        builder = JDABuilder.createDefault(Configuration.getProperty("bot.token"));
    }

    public void addListener(ListenerAdapter adapter) {
        if (botState != BotState.Setup) {
            logger.error("Tried to add " + adapter.getClass().getName() + " adapter after bot start!");
            return;
        }

        logger.debug("Add " + adapter.getClass().getName() + " adapter.");
        builder.addEventListeners(adapter);
    }

    public void addIntents(GatewayIntent... intents) {
        if (botState != BotState.Setup) {
            logger.error("Tried to add gateway intents after bot start!");
            return;
        }
        builder.enableIntents(List.of(intents));
    }

    public void addGuildCommands(CommandData... commands) {
        for(Guild g : jdaBot.getGuilds()) {
            addGuildCommands(g, commands);
        }
    }

    public void addGuildCommands(Guild guild, CommandData... commands) {
        if (botState != BotState.Work) {
            logger.error("Tried to add guild commands when bot haven't been started!");
            return;
        }

        guild.updateCommands().addCommands(commands).queue();
    }

    private static enum BotState {
        Setup,
        Work
    }
}
