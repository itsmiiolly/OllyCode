package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents the null expression indicating that there is no value
 * @author molenzwiebel
 */
public class NullExpression implements Expression {
	@Override
	public String toString() {
		return "null";
	}
}
