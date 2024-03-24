package ru.cornivella.discord;

import java.io.IOException;
import java.util.List;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import ru.cornivella.discord.utils.Configuration;

public class App {
    public static void main(String[] args) {
        try {
            Configuration.init();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't continue!");
            System.exit(-1);
        }

        Bot bot = new Bot();

        JDABuilder.createLight(Configuration.getProperty("bot.token"))
            .addEventListeners(bot)
            .enableIntents(List.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
            ))
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.watching("messages"))
            .build();
    }
}
