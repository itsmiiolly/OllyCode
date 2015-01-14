package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents a string literal in ollycode
 * @author molenzwiebel
 */
public class StringLiteralExpression implements Expression {
	private String value;
	
	public StringLiteralExpression(String val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
}