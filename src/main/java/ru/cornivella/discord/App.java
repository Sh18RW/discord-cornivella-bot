package ru.cornivella.discord;


import java.io.IOException;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.Solver;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException, ArithmeticErrorException {
        // Tools.getInstance();
        
        // Bot.getInstance().addListener(new MathCosAdapter());
        // Bot.getInstance().addListener(new MathSolveAdapter());
        // Bot.startBot();

        // Bot.getInstance().addGuildCommands(
        //     Commands.slash("cos", "Get cos by radian")
        //         .addOption(OptionType.NUMBER, "radians", "The radian value.", true),
        //     Commands.slash("solve", "Solve simple math expression")
        //         .addOption(OptionType.STRING, "expression", "You can use numbers, + - * / with ( )!", true)
        // );

        double value = Solver.solve("12 * (13 - 2) - 4 * 3 * 11");
        System.out.println(value);
    }
}
