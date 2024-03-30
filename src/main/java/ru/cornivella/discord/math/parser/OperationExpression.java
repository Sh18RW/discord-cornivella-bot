package ru.cornivella.discord.math.parser;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.ArithmeticSolveErrorException;
import ru.cornivella.discord.math.parser.tokens.OperatorType;
import ru.cornivella.discord.math.parser.tokens.TokenType;

public class OperationExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final OperatorType operation;

    public OperationExpression(Expression left, Expression right, OperatorType operation, String meta, boolean negative) {
        super(meta, TokenType.Operation, negative);
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public double solve() throws ArithmeticErrorException {
        double result = 0;
        switch (operation) {
            case Plus -> {
                result = left.solve() + right.solve();
            }
            case Minus -> {
                result = left.solve() - right.solve();
            }
            case Multiply -> {
                result = left.solve() * right.solve();
            }
            case Divide -> {
                double rightValue = right.solve();
                if (rightValue == 0) {
                    throw new ArithmeticSolveErrorException(meta, "dividion by zero!");
                }
                result = left.solve() / rightValue;
            }
            case Degree -> {
                result = Math.pow(left.solve(), right.solve());
            }
            default -> {
                throw new ArithmeticSolveErrorException(meta, "can't find " + operation + " operation!");
            }
        }

        if (negative)
            result *= -1;
        
            return result;
    }

    @Override
    public String toString() {
        return "[OperationExpression:" + left.toString() + " [" + operation + "] " + right.toString() + "]";
    }

}
