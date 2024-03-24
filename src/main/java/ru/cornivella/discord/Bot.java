package ru.cornivella.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.cornivella.discord.math.MathExecutor;
import ru.cornivella.discord.utils.Configuration;

/**
 * @author Sh18RW
 */
public class Bot extends ListenerAdapter {
    private final String prefix;
    public Bot() {
        prefix = Configuration.getProperty("bot.prefix");
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentRaw();
        if (content.startsWith(prefix)) {
            content = content.replaceAll("^.", "");

            String[] command = content.split(" ");

            if (command.length > 0) {
                switch (command[0]) {
                    case "cos" -> {
                        MathExecutor.cosCommand(event, command);
                    }
                    case "sum" -> {
                        MathExecutor.sumCommand(event, command);
                    }
                    default -> {
                    }
                }
            }
        }
    }
}
