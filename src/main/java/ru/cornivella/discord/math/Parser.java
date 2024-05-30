package ru.cornivella.discord.math;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.math.tools.ArgumentSeparatorToken;
import ru.cornivella.discord.math.tools.ConstantToken;
import ru.cornivella.discord.math.tools.FunctionToken;
import ru.cornivella.discord.math.tools.NumberToken;
import ru.cornivella.discord.math.tools.OperatorToken;
import ru.cornivella.discord.math.tools.ParenthesisToken;
import ru.cornivella.discord.math.tools.Token;

public class Parser
{

    private static final String letters = "qwertyuiopasdfghjklzxcvbnm";
    private static final String numbers = "1234567890.";
    private static final String parentheses = "()";
    private static final String operators = "+-/*^!";
    private static final String argumentsSeparator = ",;";

    private final Logger logger;

    @SuppressWarnings("rawtypes")
    private final List<Token> tokenList;
    private final String expression;

    // Parser state
    private StringBuilder currentWord; // reading word
    private ReadingType readingType; // currently reading type of word as number or parenthesis
    private int iterator; // current parser position
    // End parses state

    public Parser(String expression)
    {
        logger = LogManager.getLogger(getClass());

        this.tokenList = new LinkedList<>();
        this.expression = expression.replaceAll("\s*", "");

        currentWord = new StringBuilder();
        readingType = ReadingType.FirstAttempt;
        iterator = 0;

        logger.debug("was initialized with expression: ", expression);
    }

    @SuppressWarnings("rawtypes")
    public List<Token> parse() throws ArithmeticParsingErrorException
    {
        logger.debug("start parsing expression: ", expression);

        try {
            for (;iterator < expression.length();iterator++)
            {
                String ch = new String(new char[] {expression.charAt(iterator)}).toLowerCase();

                if (letters.contains(ch))
                {
                    processNextSymbol(ch, ReadingType.Word, false);
                }
                else if (numbers.contains(ch))
                {
                    processNextSymbol(ch, ReadingType.Number, false);
                }
                else if (operators.contains(ch))
                {
                    processNextSymbol(ch, ReadingType.Operator, true);
                }
                else if (parentheses.contains(ch))
                {
                    processNextSymbol(ch, ReadingType.Parenthesis, true);
                }
                else if (argumentsSeparator.contains(ch))
                {
                    processNextSymbol(ch, ReadingType.Separator, true);
                }
                else
                {
                    throw new ArithmeticParsingErrorException(String.format("Unknown symbol %s.", ch));
                }
            }

            addNextToken(); // currently reading
            logger.debug("successful parsed expression: ", expression);
        }
        catch(ArithmeticParsingErrorException e)
        {
            throw new ArithmeticParsingErrorException(String.format("Parsing exception at %d: %s", iterator, e.getMessage()));
        }
        return tokenList;
    }

    private void processNextSymbol(String ch, ReadingType waitFor, boolean once) throws ArithmeticParsingErrorException
    {
        if ((readingType != waitFor && readingType != ReadingType.FirstAttempt) || (once && readingType == waitFor))
        {
            addNextToken();
        }
        readingType = waitFor;
        currentWord.append(ch);
    }

    @SuppressWarnings("rawtypes")
    private void addNextToken() throws ArithmeticParsingErrorException
    {
        Token token;
        String value = currentWord.toString();
        if (readingType == ReadingType.Number)
        {
            token = NumberToken.makeToken(value, iterator);
        }
        else if (readingType == ReadingType.Operator)
        {
            token = OperatorToken.makeToken(value, iterator);
        }
        else if (readingType == ReadingType.Parenthesis)
        {
            token = ParenthesisToken.makeToken(value, iterator);
        }
        else if (readingType == ReadingType.Separator)
        {
            token = ArgumentSeparatorToken.makeToken(iterator);
        }
        else if (readingType == ReadingType.Word)
        {
            if (FunctionToken.isFunction(value))
            {
                token = FunctionToken.makeToken(value, iterator);
            }
            else if (ConstantToken.isConstant(value))
            {
                token = ConstantToken.makeToken(value, iterator);
            }
            else
            {
                throw new ArithmeticParsingErrorException(String.format("Can't parse unknown word '%s'.", value));
            }
        }
        else
        {
            throw new ArithmeticParsingErrorException(String.format("Unknown ReadingType '%s'.", readingType.toString()));
        }

        tokenList.add(token);
        currentWord = new StringBuilder(); // clear string buffer :)
    }

    private static enum ReadingType
    {
        Number, // each number, for example '2' or '4'
        Word, // each constant or function, for example 'cos' or 'Pi'
        Operator, // each operator, for example '+' or '*'
        Parenthesis, // parenthesis '(' and ')'
        Separator, // separator, for example 'lerp(2, 4, 0.5)' ',' is separator
        FirstAttempt // Parsing beginning
    }
}
