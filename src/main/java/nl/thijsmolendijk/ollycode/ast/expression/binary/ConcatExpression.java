package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;

/**
 * Represents both {@code +} for the adding of 2 numbers and {@code +} for concatting 2 values
 * @author thijsmolendijk
 */
public class ConcatExpression extends BinaryExpression {
	public ConcatExpression(Expression l, Expression r) {
		super("+", l, r);
	}
}
