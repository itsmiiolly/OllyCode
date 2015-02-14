package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents a number literal in ollycode. Any ollycode number is a double
 * @author molenzwiebel
 */
public class NumberLiteralExpression implements Expression {
	private double value;
	
	public NumberLiteralExpression(double val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return value + "";
	}

	@Override
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
