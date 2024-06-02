package ru.cornivella.discord.math.tools;

public abstract class Expression {
    private boolean isNegative;

    public void switchNegative()
    {
        isNegative = !isNegative;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean isNegative) {
        this.isNegative = isNegative;
    }
}
