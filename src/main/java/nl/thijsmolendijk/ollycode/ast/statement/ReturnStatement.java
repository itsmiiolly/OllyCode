package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;
import nl.thijsmolendijk.ollycode.runtime.ReturnException;

/**
 * Represents the return statement.
 * @author molenzwiebel
 */
public class ReturnStatement implements Statement {
	private Expression value;
	
	public ReturnStatement(Expression val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return "return " + value;
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		throw new ReturnException(value.eval(interpreter));
	}
}
