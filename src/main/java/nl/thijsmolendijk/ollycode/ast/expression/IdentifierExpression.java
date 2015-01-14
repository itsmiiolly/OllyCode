package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents an identifier that either refers to a variable or to a class.
 * @author molenzwiebel
 */
public class IdentifierExpression implements Expression {
	private String value;
	
	public IdentifierExpression(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
