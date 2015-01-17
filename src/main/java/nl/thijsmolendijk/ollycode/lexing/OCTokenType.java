package nl.thijsmolendijk.ollycode.lexing;

/**
 * Represents a type of a {@link OCToken}
 * @author molenzwiebel
 */
public enum OCTokenType {
	/**
	 * Indicates that the end of the file has been reached
	 */
	EOF,
	/**
	 * {@code def} keyword indicating a function definition
	 */
	DEF,
	/**
	 * {@code new} keyword indicating a new object instance
	 */
	NEW,
	/**
	 * {@code var} keyword indicating a variable definition
	 */
	VAR,
	/**
	 * {@code if} keyword indicating an if statement
	 */
	IF,
	/**
	 * {@code else} keyword indicating an else statement
	 */
	ELSE,
	/**
	 * {@code elseif} keyword indicating an elseif statement
	 */
	ELSEIF,
	/**
	 * {@code while} keyword indicating an while statement
	 */
	WHILE,
	/**
	 * {@code for} keyword indicating an for statement
	 */
	FOR,
	/**
	 * {@code return} keyword indicating a return expression
	 */
	RETURN,
	/**
	 * Indicates that the token is a numeric value. getValue() will return the corresponding {@link Double} instance
	 */
	NUMBER,
	/**
	 * Indicates that the token is an identifier (matches [a-zA-Z][a-zA-Z0-9]*). getValue() will return the corresponding identifier as a {@link String}
	 */
	IDENTIFIER,
	/**
	 * Indicates that the token is a string enclosed in {@code "}. getValue() will return the corresponding value as a {@link String}
	 */
	STRING,
	/**
	 * {@code null} keyword indicating a null expression
	 */
	NULL,
	/**
	 * {@code true} keyword indicating boolean with value {@code true}
	 */
	TRUE,
	/**
	 * {@code false} keyword indicating boolean with value {@code false}
	 */
	FALSE,
	/**
	 * {@code class} keyword indicating the start of a class
	 */
	CLASS,
	/**
	 * <code>{</code> character indicating the start of a block (such as a body or a class)
	 */
	BEGIN_BLOCK,
	/**
	 * <code>}</code> character indicating the end of a block (such as a body or a class)
	 */
	END_BLOCK, //}
	/**
	 * Indicates that the lexer did not match any token, and thus returned the character currently being looked at. getValue() will contain the char as a {@link Character} instance
	 */
	CHARACTER,
	/**
	 * <code>>=</code> binary expression
	 */
	GT_EQ,
	/**
	 * <code><=</code> binary expression
	 */
	LT_EQ,
	/**
	 * <code>&&</code> binary expression
	 */
	AND,
	/**
	 * <code>||</code> binary expression
	 */
	OR,
	/**
	 * <code>==</code> binary expression
	 */
	EQUAL,
	/**
	 * <code>!=</code> binary expression
	 */
	NOT_EQUAL;
	
	public static int getTokenPrecedence(char token) {
		return -1;
	}
}
