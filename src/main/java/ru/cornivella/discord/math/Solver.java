package ru.cornivella.discord.math;

import ru.cornivella.discord.math.tools.ArgumentExpression;
import ru.cornivella.discord.math.tools.ConstantExpression;
import ru.cornivella.discord.math.tools.ConstantType;
import ru.cornivella.discord.math.tools.Expression;
import ru.cornivella.discord.math.tools.FunctionExpression;
import ru.cornivella.discord.math.tools.FunctionType;
import ru.cornivella.discord.math.tools.NumberExpression;
import ru.cornivella.discord.math.tools.OperationExpression;
import ru.cornivella.discord.math.tools.OperationType;

public class Solver {
    private final TokenTree tokenTree;

    public Solver(TokenTree tokenTree) {
        this.tokenTree = tokenTree;
    }

    public double getResult() throws ArithmeticErrorException {
        return getExpressionValue(tokenTree.getRoot());
    }

    private double getExpressionValue(Expression expression) throws ArithmeticErrorException {
        if (expression instanceof FunctionExpression) {
            return getFunctionValue((FunctionExpression) expression);
        } else if (expression instanceof ConstantExpression) {
            return getConstantValue((ConstantExpression) expression);
        } else if (expression instanceof NumberExpression) {
            return ((NumberExpression) expression).getNumberToken().getValue();
        } else if (expression instanceof OperationExpression) {
            return getOperationValue((OperationExpression) expression);
        }

        throw new ArithmeticErrorException(String.format("Can't get %s expression value.", expression));
    }

    private double getOperationValue(OperationExpression operationExpression) throws ArithmeticErrorException {
        OperationType operationType = operationExpression.getOperatorToken().getValue();
        double right = getExpressionValue(operationExpression.getRightExpression());
        if (operationType == OperationType.Factorial) {
            double number = 1;
            if (right < 0) {
                throw new ArithmeticErrorException(String.format("Can't get factorial of negative value at %d.", operationExpression.getOperatorToken().getTracer()));
            }

            for (int i = 2;number <= right;i++) {
                number *= i;
            }

            return number;
        }

        double left = getExpressionValue(operationExpression.getLeftExpression());

        if (operationType == OperationType.Plus) {
            return left + right;
        } else if (operationType == OperationType.Minus) {
            return left - right;
        } else if (operationType == OperationType.Multiply) {
            return left * right;
        }  else if (operationType == OperationType.Divide) {
            if (right == 0) {
                throw new ArithmeticErrorException(String.format("Division by 0 on %d.",
                    operationExpression.getOperatorToken().getTracer()));
            }

            return left / right;
        } else if (operationType == OperationType.Degree) {
            return Math.pow(left, right);
        }

        throw new ArithmeticErrorException(String.format("Can't get %s operation value.", operationType));
    }

    private double getFunctionValue(FunctionExpression functionExpression) throws ArithmeticErrorException {
        FunctionType functionType = functionExpression.getFunctionToken().getValue();

        if (functionType == FunctionType.Cos) {
            double[] arguments = getArguments(functionExpression, 1);
            return Math.cos(arguments[0]);
        } else if (functionType == FunctionType.Sin) {
            double[] arguments = getArguments(functionExpression, 1);
            return Math.sin(arguments[0]);
        } else if (functionType == FunctionType.Tg) {
            double[] arguments = getArguments(functionExpression, 1);
            return Math.tan(arguments[0]);
        } else if (functionType == FunctionType.Ctg) {
            double[] arguments = getArguments(functionExpression, 1);
            double tan = Math.tan(arguments[0]);
            if (tan == 0) {
                throw new ArithmeticErrorException(String.format("Division by 0 on %d. Ctg can't be of Pi/2 + 2Pi * k, k is Z!",
                    functionExpression.getFunctionToken().getTracer()));
            }

            return 1 / Math.tan(arguments[0]);
        }

        throw new ArithmeticErrorException(String.format("Can't get %s function value.", functionType));
    }

    private double getConstantValue(ConstantExpression constantExpression) throws ArithmeticErrorException {
        ConstantType constantType = constantExpression.getConstantToken().getValue();

        if (constantType == ConstantType.Pi) {
            return Math.PI;
        }

        throw new ArithmeticErrorException(String.format("Can't get %s constant value.", constantType));
    }

    private double[] getArguments(FunctionExpression functionExpression, int count) throws ArithmeticErrorException {
        double[] arguments = new double[count];
        Expression argument = functionExpression.getArguments();

        while (count > 0) {
            if (argument == null) {
                throw new ArithmeticErrorException(String.format("You missed the argument in function at ",
                functionExpression.getFunctionToken().getTracer()));
            }
            double value;

            if (argument instanceof ArgumentExpression) {
                ArgumentExpression argumentExpression = (ArgumentExpression) argument;

                value = getExpressionValue(argumentExpression.getArgument());
                argument = argumentExpression.getNextArgument();
            } else {
                value = getExpressionValue(argument);
                argument = null;
            }

            arguments[--count] = value;
        }
        
        return arguments;
    }
}
