package ru.cornivella.discord.parser;

public class ArithmeticErrorException extends Exception {
    public ArithmeticErrorException(String reason) {
        super(reason);
    }
}
