package ru.cornivella.discord.math;

import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.math.tools.ArgumentSeparatorToken;
import ru.cornivella.discord.math.tools.ConstantToken;
import ru.cornivella.discord.math.tools.Expression;
import ru.cornivella.discord.math.tools.FunctionToken;
import ru.cornivella.discord.math.tools.NumberExpression;
import ru.cornivella.discord.math.tools.NumberToken;
import ru.cornivella.discord.math.tools.OperatorToken;
import ru.cornivella.discord.math.tools.ParenthesisToken;
import ru.cornivella.discord.math.tools.Token;
import ru.cornivella.discord.math.tools.TokenType;

public class TokenTree {
    @SuppressWarnings("rawtypes")
    private final List<Token> tokenList;
    private final Logger logger;

    private final Stack<Expression> expressionStack = new Stack<>();

    private Expression root;
    private int openedParenthesis;
    private ReadingType readingType;

    @SuppressWarnings("rawtypes")
    public TokenTree(List<Token> tokenList) {
        logger = LogManager.getLogger(getClass());
        this.tokenList = tokenList;

        openedParenthesis = 0;
        readingType = ReadingType.Expression;
    }

    // Here is some magic :) I will try to make it some readable
    @SuppressWarnings("rawtypes")
    public void build() throws ArithmeticParsingErrorException {
        logger.debug(String.format("Starting building tree with %s tokens.", tokenList.toString()));
        expressionStack.add(new NumberExpression());

        for (Token token : tokenList) {
            TokenType tokenType = token.getTokenType();

            if (tokenType == TokenType.Number) {
                processNumber((NumberToken) token);
            } else if (tokenType == TokenType.Operator) {
                processOperator((OperatorToken) token);
            } else if (tokenType == TokenType.Parentheses) {
                processParenthesis((ParenthesisToken) token);
            } else if (tokenType == TokenType.Constant) {
                processConstant((ConstantToken) token);
            } else if (tokenType == TokenType.Function) {
                processFunction((FunctionToken) token);
            } else if (tokenType == TokenType.ArgumentsSeparator) {
                processArgumentSeparator((ArgumentSeparatorToken) token);
            }
        }

        if (openedParenthesis != 0) {
            throw new ArithmeticTokenTreeException(String.format("%d parenthesis was not closed.", openedParenthesis));
        }

        root = expressionStack.firstElement();
        logger.debug(String.format("TokenTree was successful built %s.", expressionStack.firstElement().toString()));
    }

    private void processNumber(NumberToken numberToken) {
    }

    private void processOperator(OperatorToken operatorToken) {
    }

    private void processParenthesis(ParenthesisToken parenthesisToken) {
    }

    private void processFunction(FunctionToken functionToken) {
    }

    private void processConstant(ConstantToken constantToken) {
    }

    private void processArgumentSeparator(ArgumentSeparatorToken argumentSeparatorToken) {
    }

    public Expression getRoot() {
        return root;
    }

    private enum ReadingType {
        Expression, // number or parenthesis, functions or constants
        Operator, // any operator
    }
}
