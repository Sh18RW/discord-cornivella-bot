package ru.cornivella.discord.math.parser;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;
import ru.cornivella.discord.math.parser.tokens.NumberToken;
import ru.cornivella.discord.math.parser.tokens.OperatorToken;
import ru.cornivella.discord.math.parser.tokens.ParenthesisToken;
import ru.cornivella.discord.math.parser.tokens.Token;
import ru.cornivella.discord.math.parser.tokens.TokenType;

public class Parser {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public static Expression parse(String expresstion) throws ArithmeticParsingErrorException {
        logger.debug("Try to parse '" + expresstion + "'.");
        return null;
    }

    private static final class ParserState {
        private static final String numberSet = "0123456789";
        private static final String operationSet = "-+*/^";
        private static final String functionSet = "qwertyuiopasdfghjklzxcvbnm";

        private final String expression; // contains full expression to parse
        private StringBuilder word; // contains current parsing word
        private int index; // value with current index of character in expression

        public ParserState(String expression) {
            this.expression = expression;
            this.index = 0;
        }

        public List<Token> getTokenList() throws ArithmeticParsingErrorException {
            TokenType currentReadingType = TokenType.Number;
            int currentOpenedParenthesis = 0;

            for (;index < expression.length();index++) {
                char ch = expression.charAt(index);

                if (characterContains(numberSet, ch)) {

                }
            }
            return null;
        }

        /*
         * Parse next token.
         * @param type contains token type, which will be parsed. If it is not this type, throws exception.
         */
        private final Token parseNextToken(TokenType type) throws ArithmeticParsingErrorException {
            String value = word.toString(); // get value to clear StringBuilder
            word = new StringBuilder(); // clears string builder.

            if (value.isEmpty()) {
                throw new ArithmeticParsingErrorException("value is empty!", getTrace());
            }

            switch (type) {
                case Number -> {
                    if (charactersContainsIn(numberSet, value))
                        return NumberToken.parse(value, getTrace());
                    
                    throw new ArithmeticParsingErrorException(value + " is not a number!", getTrace());
                }
                case Operation -> {
                    if (charactersContainsIn(operationSet, value))
                        return OperatorToken.parse(value, getTrace());
                }
                case Function -> {
                    // if (charactersContainsIn(functionSet, value))
                    //     return FunctionToken.parse(value, getTrace());
                    throw new ArithmeticParsingErrorException("Math functions not supported yet.", getTrace());
                }
                case Parenthesis -> {
                    if (charactersContainsIn("()", value))
                        return ParenthesisToken.parse(value, getTrace());
                }
                default -> {
                    throw new ArithmeticParsingErrorException("can't find " + type + " type parser.", getTrace());
                }
            }
            return null; // can't be reached.
        }

        private final String getTrace() {
            return "At " + index + " of '" + expression + "': ";
        }

        /*
         * @returns If ch contains in set, returns true
         */
        private static final boolean characterContains(String set, char ch) {
            return set.indexOf(ch) != -1;
        }

        /*
         * @return If all characters contains in set, returns true
         * @todo Maybe I should remade it
         */
        private static final boolean charactersContainsIn(String set, String characters) {
            for (char ch : characters.toCharArray()) {
                if (!characterContains(set, ch)) {
                    return false;
                }
            }

            return true;
        }
    }
}
