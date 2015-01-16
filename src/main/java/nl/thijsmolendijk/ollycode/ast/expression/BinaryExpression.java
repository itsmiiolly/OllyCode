package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;

/**
 * Represents a binary operation containing a left expression, an operation and a right expression.
 * Examples are 1 + 2, where 1 is the left expression, + is the operator and 2 is the right expression.
 * @author molenzwiebel
 */
public abstract class BinaryExpression implements Expression {
	private Expression left;
	private Expression right;
	private String operation;

	public BinaryExpression(String o, Expression l, Expression r) {
		this.operation = o;
		this.left = l;
		this.right = r;
	}

	@Override
	public String toString() {
		return left + " " + operation + " " + right;
	}
}
