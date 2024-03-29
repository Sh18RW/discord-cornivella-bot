package ru.cornivella.discord.parser;

public class ArithmeticParsingErrorException extends ArithmeticErrorException {
    public ArithmeticParsingErrorException(String reason) {
        super(reason);
    }
}
