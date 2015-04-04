package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

public class AssignmentExpression extends BinaryExpression {
	public AssignmentExpression(Expression left, Expression right) {
		super(left, "=", right);
		assert(left instanceof IdentifierExpression || left instanceof MemberExpression);
	}
}
