package ru.cornivella.discord;


import java.io.IOException;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.parser.Parser;
import ru.cornivella.discord.math.parser.TokenTree;

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

        TokenTree value = Parser.parse("((((2^2^3^2/(2 + (2^6 - 2))) / (32 * 2))^(10 * 10))*10^2^2)^(1/2)*0.1*10");
        System.out.println(value);
        System.out.println(value.getRoot().solve());
    }
}
