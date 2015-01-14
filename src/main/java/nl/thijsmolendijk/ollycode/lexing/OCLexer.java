package nl.thijsmolendijk.ollycode.lexing;

/**
 * The main lexer that is responsible for analyzing the given input and splitting it up into {@link OCToken}s
 * @author molenzwiebel
 */
public class OCLexer {	
	private String input;
	private OCTokenizingRuleSet ruleSet;
	
	private int currentIndex;
	private char lastCharacter;
	
	public OCLexer(OCTokenizingRuleSet rules, String input) {
		this.ruleSet = rules;
		this.input = input;
		this.currentIndex = 0;
		this.lastCharacter = ' ';		
	}
	
	/**
	 * Gets the next token available
	 */
	public OCToken getNextToken() {
		while (lastCharacter == ' ') {
			lastCharacter = nextChar();
		}
		int startingIndex = currentIndex;

		if (Character.isLetter(lastCharacter)) { // identifier: [a-zA-Z][a-zA-Z0-9]*
			StringBuilder identifier = new StringBuilder().append(lastCharacter);
			while (Character.isLetterOrDigit((lastCharacter = nextChar())))
				identifier.append(lastCharacter);
			String id = identifier.toString();
			
			//Check for rule in OCTokenizingRuleSet
			if (ruleSet.forIdentifier(id) != null) {
				return new OCToken(ruleSet.forIdentifier(id), startingIndex, currentIndex - startingIndex);
			}
			
			return new OCToken(OCTokenType.IDENTIFIER, id, startingIndex, currentIndex - startingIndex);
		}
		
		if (lastCharacter == '"') { //String
			StringBuilder strBuilder = new StringBuilder();
			while (true) {
				char prev = lastCharacter;
				lastCharacter = nextChar();
				if (lastCharacter == -1) throw new RuntimeException("Unclosed string");
				
				//Handle escaping of " using \"
				if (lastCharacter == '"' && prev != '\\') break;
				if (lastCharacter == '"' && prev == '\\') {
					strBuilder = new StringBuilder(strBuilder.substring(0, strBuilder.length() - 1));
				}
				
				strBuilder.append(lastCharacter);
			}
			lastCharacter = nextChar();
			return new OCToken(OCTokenType.STRING, strBuilder.toString(), startingIndex, currentIndex - startingIndex);
		}

		if (Character.isDigit(lastCharacter)) {   // Number: [0-9.]+
			StringBuilder numberBuilder = new StringBuilder();
			do {
				numberBuilder.append(lastCharacter);
				lastCharacter = nextChar();
			} while (Character.isDigit(lastCharacter) || lastCharacter == '.');

			return new OCToken(OCTokenType.NUMBER, Double.parseDouble(numberBuilder.toString()), startingIndex, currentIndex - startingIndex);
		}

		if (lastCharacter == '#' || (lastCharacter == '/' && peekChar() == '/')) {
			// Comment until end of line.
			do lastCharacter = nextChar();
			while (lastCharacter != -1 && lastCharacter != '\n' && lastCharacter != '\r');

			if (lastCharacter != -1)
				return getNextToken();
		}

		// Check for end of file.  Don't eat the EOF.
		if (lastCharacter == (char) -1) {
			return new OCToken(OCTokenType.EOF, startingIndex, startingIndex);
		}
		
		// Otherwise, just return the character as its ascii value.
		char c = lastCharacter;
		lastCharacter = nextChar();
		
		//Check for rule in OCTokenizingRuleSet
		OCTokenType type = null;
		if ((type = ruleSet.forChar(c)) != null) return new OCToken(type, startingIndex, startingIndex);
		
		return new OCToken(OCTokenType.CHARACTER, c, startingIndex, startingIndex);
	}
	
	/**
	 * @return the input this lexer is parsing
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * @return the next character to be looked at, as indicated by the {@code currentIndex} variable. Returns -1 if EOF
	 */
	public char nextChar() {
		if (currentIndex >= input.length())
			return (char) -1;
		return input.charAt(currentIndex++);
	}

	/**
	 * Same as {@link #nextChar()} but does not increment the index (only peeks)
	 */
	public char peekChar() {
		if (currentIndex + 1 >= input.length())
			return (char) -1;
		return input.charAt(currentIndex+1);
	}
}
