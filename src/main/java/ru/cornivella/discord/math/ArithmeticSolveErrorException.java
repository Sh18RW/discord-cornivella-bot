package ru.cornivella.discord.math;

public class ArithmeticSolveErrorException extends ArithmeticErrorException {

    public ArithmeticSolveErrorException(String reason) {
        super(reason);
    }
    public ArithmeticSolveErrorException(String meta, String reason) {
        this(meta + ": " + reason);
    }

}
