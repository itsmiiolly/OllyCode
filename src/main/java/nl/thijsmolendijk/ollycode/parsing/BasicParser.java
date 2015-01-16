package nl.thijsmolendijk.ollycode.parsing;

import nl.thijsmolendijk.ollycode.lexing.OCLexer;
import nl.thijsmolendijk.ollycode.lexing.OCToken;
import nl.thijsmolendijk.ollycode.lexing.OCTokenType;
import nl.thijsmolendijk.ollycode.lexing.OCTokenizingRuleSet;

/**
 * Basic parsing methods for handling tokens that do not really belong in {@link OCParser} 
 * @author molenzwiebel
 */
public abstract class BasicParser {
	private OCLexer lexer;
	
	protected OCToken currentToken;
	protected OCToken previousToken;
	
	public BasicParser(String input, OCTokenizingRuleSet rules) {
		this.lexer = new OCLexer(rules, input);
	}
	
	/**
	 * Made public for the RPPL.
	 * @return the current token
	 */
	public OCToken getCurrentToken() {
		return currentToken;
	}
	
	/**
	 * Gets the next token from the lexer and assigns the {@code currentToken} and {@code previousToken} variables
	 * @return the new token
	 */
	public OCToken nextToken() {
		previousToken = currentToken;
		return currentToken = lexer.getNextToken();
	}
	
	/**
	 * Checks if the current token is of type CHARACTER with the specified character value
	 * @param c the character to compare to
	 * @return if the current token is the provided character
	 */
	protected boolean isChar(char c) {
		return isChar(c, currentToken);
	}
	
	/**
	 * @see #isChar(char)
	 */
	protected boolean isChar(char c, OCToken token) {
		return token != null && token.getType() == OCTokenType.CHARACTER && token.getValue().equals(c);
	}
	
	/**
	 * Checks if the current token is of the specified type
	 * @param type the type to check
	 * @return if the token type is equal
	 */
	protected boolean isType(OCTokenType type) {
		return isType(type, currentToken);
	}
	
	/**
	 * @see #isType(OCTokenType)
	 */
	protected boolean isType(OCTokenType type, OCToken token) {
		return token != null && token.getType() == type;
	}
	
	/**
	 * @see BasicParser#expect(OCTokenType)
	 */
	protected void expect(char c) {
		expect(c, "Expected "+c+". Received %s");
	}
	
	/**
	 * @see #expect(OCTokenType, String)
	 */
	protected void expect(char c, String errorMessage) {
		if (currentToken != null && currentToken.getType() == OCTokenType.CHARACTER && currentToken.getValue().equals(c)) return;
		showErrorMessage(errorMessage, currentToken.toString());
	}
	
	/**
	 * Indicates that the parser expects the currentToken to be of the provided type
	 * @param type the type
	 */
	protected void expect(OCTokenType type) {
		expect(type, "[-] Expected "+type.name()+". Received %s");
	}
	
	/**
	 * Indicates that the parser expects the currentToken to be of the provided type
	 * @param type the type
	 * @param errorMessage the error to be shown when the token is not of the specified type
	 */
	protected void expect(OCTokenType type, String errorMessage) {
		if (currentToken != null && currentToken.getType() == type) return;
		showErrorMessage(errorMessage, currentToken.toString());
	}
	
	/**
	 * Indicates that the parser expects the next token to be the provided char
	 * @param c the char
	 */
	protected void expectNext(char c) {
		nextToken(); //Advance to next token
		expect(c);
	}
	
	/**
	 * Indicates that the parser expects the next token to be the provided char
	 * @param c the char
	 * @param errorMessage the error to be shown when the token is not the char
	 */
	protected void expectNext(char c, String errorMessage) {
		nextToken(); //Advance to next token
		expect(c, errorMessage);
	}
	
	/**
	 * Indicates that the parser expects the next token to be of the provided type
	 * @param type the type
	 */
	protected void expectNext(OCTokenType type) {
		nextToken(); //Advance to next token
		expect(type);
	}
	
	/**
	 * Indicates that the parser expects the next token to be of the provided type
	 * @param type the type
	 * @param errorMessage the error to be shown when the token is not of the specified type
	 */
	protected void expectNext(OCTokenType type, String errorMessage) {
		nextToken(); //Advance to next token
		expect(type, errorMessage);
	}
	
	/**
	 * Helper method that shows an error message in red text along with a slice of where it happened.
	 * @param format the message format
	 * @param objects the formatting objects
	 */
	protected void showErrorMessage(String format, Object... objects) {
		int beginIndex = currentToken.getStartIndex() - 10 < 0 ? 0 : currentToken.getStartIndex() - 10;
		int endIndex = currentToken.getEndIndex() + 10 >= lexer.getInput().length() ? lexer.getInput().length() : currentToken.getEndIndex() + 10;
		
		String codeSlice = lexer.getInput().substring(beginIndex, endIndex);
		System.err.println("\033[31m[-] " + String.format(format, objects)); //Print error in red
		System.err.println("\033[31m[-] At: "+codeSlice + "\033[0m");
		System.exit(-1);
	}
}
