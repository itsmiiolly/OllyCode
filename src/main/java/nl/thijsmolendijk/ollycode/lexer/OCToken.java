package nl.thijsmolendijk.ollycode.lexer;

/**
 * A Token is an interesting part in the source code. Tokens are provided by the {@link OCLexer}
 * @author molenzwiebel
 */
public class OCToken {
	/**
	 * The type of this token.
	 */
	private OCTokenType type;
	/**
	 * The optional value of this token, such as the numeric value.
	 */
	private Object value;
	
	/**
	 * The index in the source code where this token starts.
	 */
	private int start;
	/**
	 * How many characters this token takes. 
	 */
	private int size;
	
	OCToken(OCTokenType type, Object value, int start, int size) {
		this.type = type;
		this.value = value;
		
		this.start = start;
		this.size = size;
	}
	
	OCToken(OCTokenType type, int start, int size) {
		this(type, null, start, size);
	}
	
	OCToken(OCTokenType type, int start) {
		this(type, start, 1);
	}
	
	/**
	 * @return the type of this token
	 */
	public OCTokenType getType() {
		return this.type;
	}
	
	/**
	 * @return the value of this token, or null if not applicable
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}
	
	/**
	 * @return the index in the source code where this token starts
	 */
	public int getStart() {
		return this.start;
	}
	
	/**
	 * @return the amount of characters this token takes
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * @return the index in the source code where this token ends
	 */
	public int getEnd() {
		return this.getStart() + this.getSize();
	}
	
	/**
	 * @return if the type of this token is the same as the provided type
	 */
	public boolean isType(OCTokenType type) {
		return this.type == type;
	}
	
	/**
	 * @return if the character that this token identifies is the same as the provided char
	 */
	public boolean isChar(char c) {
		return this.type == OCTokenType.CHARACTER && this.value != null && this.value instanceof Character && this.value.equals(c);
	}
	
	/**
	 * @return if the value of this token is the same as the provided value
	 */
	public boolean isValue(Object val) {
		return this.type != OCTokenType.EOF && this.type != null && this.type.equals(val);
	}
	
	@Override
	public String toString() {
		return String.format("source:%d:%d:%d: %s (%s)", this.getStart(), this.getSize(), this.getEnd(), this.getType().name(), this.value == null ? "''" : this.value.toString());
	}
	
	@Override
	public int hashCode() {
		return 7 * this.type.ordinal()
			+ 19 * (this.value == null ? 3 : this.value.hashCode())
			+ 23 * (this.start + this.size);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OCToken)) return false;
		OCToken otherToken = (OCToken) other;
		if (otherToken.type != this.type) return false;
		if (otherToken.size != this.size) return false;
		if (otherToken.start != this.start) return false;
		if (otherToken.value == null && this.value == null) return true;
		return this.value.equals(otherToken.value);
	}
}
