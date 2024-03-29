package ru.cornivella.discord.math;

import ru.cornivella.discord.math.parser.Parser;

public class Solver {
    public static double solve(String expresstion) throws ArithmeticParsingErrorException, ArithmeticErrorException {
        return Parser.getInstance().parse(expresstion).solve();
    }
}
