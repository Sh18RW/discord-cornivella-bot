package ru.cornivella.discord.parser.parser;

import ru.cornivella.discord.parser.ArithmeticErrorException;

public abstract class Expression {
    protected final String meta;

    public Expression(String meta) {
        this.meta = meta;
    }

    public abstract double solve() throws ArithmeticErrorException;
}
