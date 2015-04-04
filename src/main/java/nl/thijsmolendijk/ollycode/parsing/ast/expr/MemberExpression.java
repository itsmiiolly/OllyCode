package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class MemberExpression implements Expression {
	public Expression parent;
	public Expression child;
	
	public MemberExpression(Expression parent, Expression child) {
		this.parent = parent;
		this.child = child;
	}
	
	@Override
	public String toString() {
		return parent + "." + child;
	}
}
