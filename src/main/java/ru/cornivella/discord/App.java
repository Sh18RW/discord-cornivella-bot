package ru.cornivella.discord;


import java.io.IOException;

import ru.cornivella.discord.parser.ArithmeticErrorException;
import ru.cornivella.discord.parser.Solver;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException, ArithmeticErrorException {
        // Tools.getInstance();
        
        // Bot.getInstance().addListener(new MathCosAdapter());
        // Bot.startBot();

        // Bot.getInstance().addGuildCommands(
        //     Commands.slash("cos", "Get cos by radian")
        //         .addOption(OptionType.NUMBER, "radians", "The radian value.", true)
        // );

        System.out.println(Solver.solve("3+7*5-45/4"));
    }
}
