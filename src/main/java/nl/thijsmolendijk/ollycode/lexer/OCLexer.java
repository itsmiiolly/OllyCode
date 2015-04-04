package nl.thijsmolendijk.ollycode.lexer;

import static nl.thijsmolendijk.ollycode.lexer.OCTokenType.*;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that splits source files up into individual, interesting parts (also known as lexical analysis or lexing).
 * @author molenzwiebel
 */
public class OCLexer {
	//Matches any identifier. An identifier needs to start with a-z, A-Z or _ and after that can contain any character from the set a-zA-Z0-9-_.
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9-_]*)");
	//Matches any number, including ones starting with a dot and those with (negative) exponents.
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^(([1-9][0-9]*\\.?[0-9]*)|([0-9]*\\.[0-9]+))([Ee][+-]?[0-9]+)?");
	//Matches any string between single or double quotation marks, including escaping.
	private static final Pattern STRING_PATTERN = Pattern.compile("^([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
	
	private String source;
	private int currentPosition;
	
	public int currentColumn;
	public int currentLine;
	
	private char currentCharacter;
	private Stack<OCToken> tokenQueue;
	
	private HashMap<String, OCTokenType> keywords;
	private HashMap<String, OCTokenType> operators;
	
	@SuppressWarnings("serial") //Fuck serial ids
	public OCLexer(String source) {		
		this.source = source;
		this.currentPosition = 0;
		
		this.currentColumn = 0;
		this.currentLine = 1;
		
		this.currentCharacter = ' ';
		this.tokenQueue = new Stack<>();
		
		this.keywords = new HashMap<String, OCTokenType>() {{
			put("def", DEF);
			put("if", IF);
			put("for", FOR);
			put("else", ELSE);
			put("while", WHILE);
			put("new", NEW);
			put("var", VAR);
			put("class", CLASS);
			put("return", RETURN);
			
			put("and", AND);
			put("or", OR);
			
			put("true", TRUE);
			put("false", FALSE);
			put("null", NULL);
		}};
		
		// Note: The order is extremely important here! We stop matching
		//       after we found an operator, so we want to match `<=` before
		//       we match `<`, otherwise we would match `<` and `=`.
		this.operators = new HashMap<String, OCTokenType>() {{
			put("::", CLASS_EXTEND);
			
			put("==", EQ);
			put("=", ASSIGN);
			put("!=", N_EQ);
			put(">=", GT_EQ);
			put(">", GT);
			put("<=", LT_EQ);
			put("<", LT);
			put("&&", AND);
			put("||", OR);
		}};
	}
	
	/**
	 * Parses the next token from the input and advances so that the index is now pointing to the token after the one returned.
	 * @return the parsed token.
	 */
	public OCToken nextToken() {
		if (!tokenQueue.isEmpty()) return tokenQueue.pop();
		while (Character.isWhitespace(currentCharacter)) currentCharacter = nextChar();

		if (currentCharacter == (char) -1) {
			return new OCToken(OCTokenType.EOF, currentPosition, 0);
		}
		
		int tokenStart = currentPosition;
		String restOfSource = source.substring(currentPosition - 1);
		
		Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher(restOfSource);
		if (identifierMatcher.find()) {
			String identifier = identifierMatcher.group(0);
			currentPosition += identifierMatcher.end() - 1;
			currentCharacter = nextChar();
			OCTokenType type = IDENTIFIER;
			if (keywords.containsKey(identifier)) type = keywords.get(identifier);
			return new OCToken(type, identifier, tokenStart, identifierMatcher.end());
		}
		
		Matcher numberMatcher = NUMBER_PATTERN.matcher(restOfSource);
		if (numberMatcher.find()) {
			String number = numberMatcher.group(0);
			currentPosition += numberMatcher.end() - 1;
			currentCharacter = nextChar();
			return new OCToken(NUMBER, Double.parseDouble(number), tokenStart, numberMatcher.end());
		}
		
		Matcher stringMatcher = STRING_PATTERN.matcher(restOfSource);
		if (stringMatcher.find()) {
			String string = stringMatcher.group(0);
			currentPosition += stringMatcher.end() - 1;
			currentCharacter = nextChar();
			return new OCToken(STRING, string, tokenStart, stringMatcher.end());
		}
		
		Optional<Entry<String, OCTokenType>> operator = operators.entrySet().stream().filter(x -> restOfSource.indexOf(x.getKey()) == 0).findFirst();
		if (operator.isPresent()) {
			currentPosition += operator.get().getKey().length() - 1;
			currentCharacter = nextChar();
			return new OCToken(operator.get().getValue(), tokenStart, operator.get().getKey().length());
		}
	
		char c = currentCharacter;
		currentCharacter = nextChar();
		return new OCToken(CHARACTER, c, tokenStart, 1);
	}
	
	/**
	 * Parses the next token, but does not advance the lexer. This has the advantage that you can "peek" to see what the next token will be.
	 * @return the next token
	 */
	public OCToken peekToken() {
		if (!tokenQueue.isEmpty()) return tokenQueue.peek();
		
		OCToken token = nextToken();
		tokenQueue.push(token);
		return token;
	}
		
	/**
	 * @return the next character to be looked at, as indicated by the {@code currentPosition} variable. Returns -1 if EOF
	 */
	public char nextChar() {
		if (currentPosition >= source.length())
			return (char) -1;
		
		char theChar = source.charAt(currentPosition++);
		if (theChar != '\n') {
			currentColumn++;
		} else {
			currentLine++;
			currentColumn = 0;
		}
		return theChar;
	}

	/**
	 * Same as {@link #nextChar()} but does not increment the index (only peeks)
	 */
	public char peekChar() {
		if (currentPosition + 1 >= source.length())
			return (char) -1;
		return source.charAt(currentPosition);
	}
}
