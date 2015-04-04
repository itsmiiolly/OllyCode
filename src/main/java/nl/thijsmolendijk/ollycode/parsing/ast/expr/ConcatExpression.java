package nl.thijsmolendijk.ollycode.parsing.ast.expr;

import nl.thijsmolendijk.ollycode.parsing.ast.Expression;

/**
 * Represents both {@code +} for the adding of 2 numbers and {@code +} for concatting 2 values
 * @author thijsmolendijk
 */
public class ConcatExpression extends BinaryExpression {
	public static final int PRECEDENCE = 15;
	
	public ConcatExpression(Expression l, Expression r) {
		super(l, "+", r);
	}	
}
