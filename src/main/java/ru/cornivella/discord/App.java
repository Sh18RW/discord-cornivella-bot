package ru.cornivella.discord;


import java.io.IOException;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ru.cornivella.discord.commands.math.MathCosAdapter;
import ru.cornivella.discord.commands.math.MathSolveAdapter;
import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.tools.Bot;
import ru.cornivella.discord.tools.Tools;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException, ArithmeticErrorException {
        Tools.getInstance();
        
        Bot.getInstance().addListener(new MathCosAdapter());
        Bot.getInstance().addListener(new MathSolveAdapter());
        Bot.startBot();

        Bot.getInstance().addGuildCommands(
            Commands.slash("cos", "Get cos by radian")
                .addOption(OptionType.NUMBER, "radians", "The radian value.", true),
            Commands.slash("solve", "Solve simple math expression")
                .addOption(OptionType.STRING, "expression", "You can use numbers, + - * / with ( )!", true)
        );
    }
}
