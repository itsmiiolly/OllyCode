package nl.thijsmolendijk.ollycode.ast.expression;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;
import nl.thijsmolendijk.ollycode.runtime.OCString;

/**
 * Represents a string literal in ollycode
 * @author molenzwiebel
 */
public class StringLiteralExpression implements Expression {
	private String value;
	
	public StringLiteralExpression(String val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return "\"" + value + "\"";
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		return new OCString(value);
	}
}
