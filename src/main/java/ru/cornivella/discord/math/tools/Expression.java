package ru.cornivella.discord.math.tools;

public abstract class Expression {
    private boolean isNegative;

    public void switchNegative()
    {
        isNegative = !isNegative;
    }
}
