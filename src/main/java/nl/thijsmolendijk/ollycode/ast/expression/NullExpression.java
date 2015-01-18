package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCNull;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents the null expression indicating that there is no value
 * @author molenzwiebel
 */
public class NullExpression implements Expression {
	@Override
	public String toString() {
		return "null";
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		return OCNull.INSTANCE;
	}
}
