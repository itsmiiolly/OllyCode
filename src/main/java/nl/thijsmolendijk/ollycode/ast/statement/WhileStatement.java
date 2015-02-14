package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;

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
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
