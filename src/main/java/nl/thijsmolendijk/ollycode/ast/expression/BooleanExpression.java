package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents a boolean expression like true or false.
 * @author molenzwiebel
 */
public class BooleanExpression implements Expression {
	private boolean value;
	
	public BooleanExpression(boolean val) {
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
