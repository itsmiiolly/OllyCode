package nl.thijsmolendijk.ollycode.lexing;

/**
 * Represents a token that has been parsed from the input. A token is any notable substring taht our parser might want to take a look at.
 * A token has a type and optionally a value attached to it
 * @author molenzwiebel
 */
public class OCToken {
	private OCTokenType type;
	private Object value;

	private int tokenStartIndex;
	private int tokenEndIndex;
	
	public OCToken(OCTokenType type, int start, int end) {
		this(type, null, start, end);
	}
	
	public OCToken(OCTokenType type, Object value, int start, int end) {
		this.type = type;
		this.value = value;
		this.tokenStartIndex = start;
		this.tokenEndIndex = end;
	}
	
	/**
	 * @return the type of this token
	 */
	public OCTokenType getType() {
		return type;
	}
	
	/**
	 * @return the value or this token, or null if not applicable
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}
	
	/**
	 * @return the index in the source where this token starts so that {@code source.substring(getStartIndex(), getEndIndex())} contains the entire token.
	 * @see OCToken#getEndIndex()
	 */
	public int getStartIndex() {
		return tokenStartIndex;
	}
	
	/**
	 * @return the index in the source where this token ends so that {@code source.substring(getStartIndex(), getEndIndex())} contains the entire token.
	 * @see OCToken#getStartIndex()
	 */
	public int getEndIndex() {
		return tokenEndIndex;
	}
	
	@Override
	public String toString() {
		return value == null ? type.name() : type.name() + " ('" + value + "')";
	}
}
