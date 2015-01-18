package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;
import nl.thijsmolendijk.ollycode.runtime.ReturnException;

/**
 * Represents the while statement in OllyCode.
 * @author molenzwiebel
 */
public class WhileStatement implements Statement {
	private Expression condition;
	private BodyStatement body;
	
	public WhileStatement(Expression cond, BodyStatement body) {
		this.condition = cond;
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "while (" + condition + ") {\n" + body + "\n}";
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		try {
			while (condition.eval(interpreter).toBoolean().booleanValue()) {
				body.eval(interpreter);
			}
			return null;
		} catch (ReturnException ex) {
			return ex.getReturn();
		}
	}
}
