package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCNumber;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

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
	public OCObject eval(Interpreter interpreter) {
		return new OCNumber(value);
	}
}
