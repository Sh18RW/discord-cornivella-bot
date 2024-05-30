package ru.cornivella.discord;


import java.io.IOException;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.Parser;

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

        System.out.println(new Parser("(2 + 2 / 2) *- 3 ^ 2! * Pi * cos(3) * -1.12").parse());
    }
}
