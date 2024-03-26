package ru.cornivella.discord.parser.parser;

import ru.cornivella.discord.parser.ArithmeticErrorException;
import ru.cornivella.discord.parser.ArithmeticSolveErrorException;

public class OperationExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final OperationType operation;

    public OperationExpression(Expression left, Expression right, OperationType operation, String meta) {
        super(meta);
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public double solve() throws ArithmeticErrorException {
        switch (operation) {
            case Plus -> {
                return left.solve() + right.solve();
            }
            case Minus -> {
                return left.solve() - right.solve();
            }
            case Multiply -> {
                return left.solve() * right.solve();
            }
            case Divide -> {
                double rightValue = right.solve();
                if (rightValue == 0) {
                    throw new ArithmeticSolveErrorException(meta, "dividion by zero!");
                }
                return left.solve() / rightValue;
            }
            case Degree -> {
                return Math.pow(left.solve(), right.solve());
            }
        }

        throw new ArithmeticSolveErrorException(meta, "can't find " + operation + " operation!");
    }

}
