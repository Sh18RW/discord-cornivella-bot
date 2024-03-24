package ru.cornivella.discord;


import java.io.IOException;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ru.cornivella.discord.commands.math.MathCosAdapter;
import ru.cornivella.discord.tools.Bot;
import ru.cornivella.discord.utils.Configuration;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration.init();
        
        Bot.getInstance().addListener(new MathCosAdapter());
        Bot.startBot();

        Bot.getInstance().addGuildCommands(
            Commands.slash("cos", "Get cos by radian")
                .addOption(OptionType.NUMBER, "radians", "The radian value.", true)
        );
    }
}
