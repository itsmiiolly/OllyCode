package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;

/**
 * Represents an identifier that either refers to a variable or to a class.
 * @author molenzwiebel
 */
public class IdentifierExpression implements Expression {
	private String value;
	
	public IdentifierExpression(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		return interpreter.getVariable(value);
	}
}
