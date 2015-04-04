package nl.thijsmolendijk.ollycode.lexer;

/**
 * Any valid type an {@link OCToken} can be.
 * @author molenzwiebel
 */
public enum OCTokenType {
	// ======================= TYPES =======================
	/**
	 * End-Of-File. Indicates that the end of the file has been reached.
	 */
	EOF,
	/**
	 * A floating point number.
	 */
	NUMBER,
	/**
	 * A string value.
	 */
	STRING,
	/**
	 * True
	 */
	TRUE,
	/**
	 * False
	 */
	FALSE,
	/**
	 * Null
	 */
	NULL,
	/**
	 * An identifier, such as a variable or function name.
	 */
	IDENTIFIER,
	/**
	 * A character without a specific matching case.
	 */
	CHARACTER,
	
	// ======================= KEYWORDS =======================
	/**
	 * The def keyword.
	 */
	DEF,
	/**
	 * The return keyword.
	 */
	RETURN,
	/**
	 * The var keyword.
	 */
	VAR,
	/**
	 * The class keyword.
	 */
	CLASS,
	/**
	 * The if keyword.
	 */
	IF,
	/**
	 * The while keyword.
	 */
	WHILE,
	/**
	 * The else keyword.
	 */
	ELSE,
	/**
	 * The new keyword.
	 */
	NEW,
	/**
	 * The for keyword.
	 */
	FOR,
	
	// ======================= OPERATORS =======================
	/**
	 * The {@code ::} operator
	 */
	CLASS_EXTEND,
	/**
	 * The {@code =} operator
	 */
	ASSIGN,
	/**
	 * The {@code ==} operator
	 */
	EQ,
	/**
	 * The {@code !=} operator
	 */
	N_EQ,
	/**
	 * The {@code >} operator
	 */
	GT,
	/**
	 * The {@code >=} operator
	 */
	GT_EQ,
	/**
	 * The {@code <} operator
	 */
	LT,
	/**
	 * The {@code <=} operator
	 */
	LT_EQ,
	/**
	 * The {@code &&} operator
	 */
	AND,
	/**
	 * The {@code ||} operator
	 */
	OR;
}
