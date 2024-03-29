package ru.cornivella.discord.math;

public class ArithmeticParsingErrorException extends ArithmeticErrorException {
    public ArithmeticParsingErrorException(String reason) {
        super(reason);
    }
    public ArithmeticParsingErrorException(String meta, String reason) {
        this(meta + ": " + reason);
    }
}
