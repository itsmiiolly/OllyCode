package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.ASTElement;
import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;

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
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
