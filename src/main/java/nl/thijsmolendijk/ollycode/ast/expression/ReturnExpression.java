package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents the return expression.
 * @author molenzwiebel
 */
public class ReturnExpression implements Expression {
	private Expression value;
	
	public ReturnExpression(Expression val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return "return " + value;
	}
}
