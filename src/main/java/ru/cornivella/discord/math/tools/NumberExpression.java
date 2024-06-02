package ru.cornivella.discord.math.tools;

public class NumberExpression extends Expression {
    private NumberToken numberToken;

    public void setNumberToken(NumberToken numberToken) {
        this.numberToken = numberToken;
    }

    public NumberToken getNumberToken() {
        return numberToken;
    }

    @Override
    public String toString() {
        return String.format("[NumberExpression %s]", numberToken.toString());
    }

    @Override
    public boolean isWhole() {
        return true;
    }
}
