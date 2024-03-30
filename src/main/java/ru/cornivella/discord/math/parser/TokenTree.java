package ru.cornivella.discord.math.parser;

import java.util.List;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;
import ru.cornivella.discord.math.parser.tokens.NumberToken;
import ru.cornivella.discord.math.parser.tokens.OperatorToken;
import ru.cornivella.discord.math.parser.tokens.OperatorType;
import ru.cornivella.discord.math.parser.tokens.ParenthesisToken;
import ru.cornivella.discord.math.parser.tokens.ParenthesisType;
import ru.cornivella.discord.math.parser.tokens.Token;
import ru.cornivella.discord.math.parser.tokens.TokenType;

public class TokenTree {
    private final Expression root;

    private TokenTree(Expression root) {
        this.root = root;
    }

    public final Expression getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return "[TokenTree:" + root.toString() + "]";
    }

    public static final TokenTree makeTokenTree(List<Token> tokenList) throws ArithmeticParsingErrorException {
        if (tokenList.size() == 0) {
            throw new ArithmeticParsingErrorException("token list is empty!");
        }
        return new TokenTree(iterate(tokenList, 0, null).getResult());
    }

    /*
     * Make token tree expression. Recursion function.
     * @param until uses to set until TokenType it will be iterate. If null, parse until end.
     */
    private static final IterationState iterate(List<Token> tokenList, int index, TokenType until) throws ArithmeticParsingErrorException {
        Expression result = null;
        Expression lastExpression = null;
        OperatorToken lastOperatorToken = null;
        boolean expressionEnd = false;
        for (;index < tokenList.size();index++) {
            Token currentToken = tokenList.get(index);
            if (currentToken.getType() == TokenType.Parenthesis) {
                ParenthesisToken parenthesisToken = (ParenthesisToken) currentToken;
                if (parenthesisToken.getValue() == ParenthesisType.Close) {
                    if (until == TokenType.Operation)
                        expressionEnd = true;
                    break;
                }

                IterationState newState = iterate(tokenList, index + 1, TokenType.Parenthesis);
                Expression newExpression = newState.getResult();
                newExpression.setNegative(parenthesisToken.isNegative());
                index = newState.getIndex();

                lastExpression = newExpression;
            } else if (currentToken.getType() == TokenType.Number) {
                NumberToken numberToken = (NumberToken) currentToken;
                lastExpression = new NumberExpression(numberToken.getValue(), numberToken.getMeta(), false);
            } else if (currentToken.getType() == TokenType.Operation) {
                OperatorToken operatorToken = (OperatorToken) currentToken;
                OperatorType operatorType = operatorToken.getValue();
                if (until == TokenType.Function){
                    index--;
                    break;
                }
                if (until == TokenType.Operation && operatorType != OperatorType.Multiply && operatorType != OperatorType.Divide && operatorType != OperatorType.Degree) {
                    index--;
                    break;
                }

                if (operatorType == OperatorType.Degree) {
                    IterationState newState = iterate(tokenList, index + 1, TokenType.Function);
                    Expression newExpression = newState.getResult();
                    index = newState.getIndex();
                    lastExpression = new OperationExpression(lastExpression, newExpression, operatorType, operatorToken.getMeta(), false);

                    continue;
                } else if (operatorType == OperatorType.Multiply || operatorType == OperatorType.Divide) {
                    IterationState newState = iterate(tokenList, index + 1, TokenType.Operation);
                    Expression newExpression = newState.getResult();
                    index = newState.getIndex();
                    lastExpression = new OperationExpression(lastExpression, newExpression, operatorType, operatorToken.getMeta(), false);

                    if ((until == TokenType.Operation || until == TokenType.Parenthesis) && newState.isExpressionEnd())
                        break;
                }

                if (result == null) {
                    result = lastExpression;
                } else if (lastExpression != null) {
                    result = new OperationExpression(result, lastExpression, lastOperatorToken.getValue(), lastOperatorToken.getMeta(), false);
                }

                lastExpression = null;

                if (operatorType == OperatorType.Minus || operatorType == OperatorType.Plus)
                    lastOperatorToken = operatorToken;
            } else {
                throw new ArithmeticParsingErrorException("unknown " + currentToken + " token!", currentToken.getMeta());
            }
        }
        if (result == null) {
            result = lastExpression;
        } else if(lastExpression != null) {
            result = new OperationExpression(result, lastExpression, lastOperatorToken.getValue(), lastOperatorToken.getMeta(), false);
        }
        return new IterationState(index, result, expressionEnd);
    }

    private static final class IterationState {
        private final int index;
        private final Expression result;
        private final boolean expressionEnd;

        public IterationState(int index, Expression result, boolean expressionEnd) {
            this.index = index;
            this.result = result;
            this.expressionEnd = expressionEnd;
        }

        public final int getIndex() {
            return index;
        }

        public final Expression getResult() {
            return result;
        }

        public boolean isExpressionEnd() {
            return expressionEnd;
        }
    }
}
