package ru.cornivella.discord;


import java.io.IOException;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.Parser;
import ru.cornivella.discord.math.Solver;
import ru.cornivella.discord.math.TokenTree;

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
        TokenTree tokenTree = new TokenTree(new Parser("2 + 2 * 2").parse());
        tokenTree.build();
        Solver solver = new Solver(tokenTree);
        System.out.println(String.format("%.4f", solver.getResult()));
    }
}
