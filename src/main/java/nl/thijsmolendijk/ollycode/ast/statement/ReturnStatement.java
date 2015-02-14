package nl.thijsmolendijk.ollycode.ast.statement;

import nl.thijsmolendijk.ollycode.ast.ASTVisitor;
import nl.thijsmolendijk.ollycode.ast.Expression;
import nl.thijsmolendijk.ollycode.ast.Statement;

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
	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visitNode(this);
	}
}
