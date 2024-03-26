package ru.cornivella.discord.parser.parser;

public class NumberExpression extends Expression {

    private final double value;

    public NumberExpression(double value, String meta) {
        super(meta);
        this.value = value;
    }

    @Override
    public double solve() {
        return value;
    }

}
