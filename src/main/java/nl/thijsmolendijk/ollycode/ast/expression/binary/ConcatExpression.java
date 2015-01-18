package nl.thijsmolendijk.ollycode.ast.expression.binary;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.expression.BinaryExpression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCNumber;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents both {@code +} for the adding of 2 numbers and {@code +} for concatting 2 values
 * @author thijsmolendijk
 */
public class ConcatExpression extends BinaryExpression {
	public ConcatExpression(Expression l, Expression r) {
		super("+", l, r);
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		OCObject leftResult = left.eval(interpreter);
		OCObject rightResult = right.eval(interpreter);
		
		if (leftResult.isNumber() && rightResult.isNumber()) return new OCNumber(leftResult.toNumber().doubleValue() + rightResult.toNumber().doubleValue());
		return leftResult.toOCString().concat(rightResult.toOCString());
	}
}
