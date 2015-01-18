package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;
import nl.thijsmolendijk.ollycode.runtime.Interpreter;
import nl.thijsmolendijk.ollycode.runtime.OCObject;
import nl.thijsmolendijk.ollycode.runtime.ReturnException;

/**
 * Represents a for statement in ollycode
 * @author molenzwiebel
 */
public class ForStatement implements Statement {
	private ASTElement initialization;
	private Expression condition;
	private Expression step;
	private BodyStatement body;

	public ForStatement(ASTElement initialization, Expression cond, Expression step, BodyStatement body) {
		assert(initialization instanceof VariableDefinitionStatement || initialization instanceof Expression);
		this.initialization = initialization;
		this.condition = cond;
		this.step = step;
		this.body = body;
	}

	@Override
	public String toString() {
		return "for (" + initialization + ", " + condition + ", " + step + ") {\n" + body + "\n}";
	}

	@Override
	public OCObject eval(Interpreter interpreter) {
		try {
			initialization.eval(interpreter);
			while (condition.eval(interpreter).toBoolean().booleanValue()) {
				body.eval(interpreter);
				step.eval(interpreter);
			}
			return null;
		} catch (ReturnException ex) {
			return ex.getReturn();
		}
	}
}
